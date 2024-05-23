package com.lilyVia.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilyVia.springbootinit.annotation.AuthCheck;
import com.lilyVia.springbootinit.common.*;
import com.lilyVia.springbootinit.constant.ChartConstant;
import com.lilyVia.springbootinit.constant.CommonConstant;
import com.lilyVia.springbootinit.constant.UserConstant;
import com.lilyVia.springbootinit.exception.BusinessException;
import com.lilyVia.springbootinit.exception.ThrowUtils;
import com.lilyVia.springbootinit.manager.AIManager;
import com.lilyVia.springbootinit.manager.RedissonManager;
import com.lilyVia.springbootinit.model.dto.chart.ChartAddRequest;
import com.lilyVia.springbootinit.model.dto.chart.ChartQueryRequest;
import com.lilyVia.springbootinit.model.dto.chart.ChartUpdateRequest;
import com.lilyVia.springbootinit.model.entity.Chart;
import com.lilyVia.springbootinit.model.entity.ChartCoreData;
import com.lilyVia.springbootinit.model.entity.User;
import com.lilyVia.springbootinit.model.vo.ChartVO;
import com.lilyVia.springbootinit.rabbitMq.BiMqProducer;
import com.lilyVia.springbootinit.service.ChartCoreDataService;
import com.lilyVia.springbootinit.service.ChartService;
import com.lilyVia.springbootinit.service.UserService;
import com.lilyVia.springbootinit.utils.FileUtils;
import com.lilyVia.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 图表接口
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService ChartService;

    @Resource
    private ChartCoreDataService chartCoreDataService;

    @Resource
    private UserService userService;

    @Resource
    private AIManager aiManager;

    @Resource
    private RedissonManager redissonManager;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private BiMqProducer biMqProducer;


    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile, @Valid @NotNull @ModelAttribute ChartAddRequest chartAddRequest, @NotNull HttpServletRequest request) {

        /**
         * 用户登录校验
         */
        User loginUser = userService.getLoginUser(request);

        /**
         * 限流校验
         */
        redissonManager.doReteLimiter("genChart_" + loginUser.getUserAccount());

        log.info("genChart_{}获得执行权限", loginUser.getUserAccount());

        /**
         * 构造ai问答
         */
        String goal = chartAddRequest.getGoal();
        String name = chartAddRequest.getName();
        String chartType = chartAddRequest.getChartType();
        String csv = FileUtils.getFileString(multipartFile);
        String aiQuestion = ChartService.getAiQuestion(csv, goal, chartType);

        // 调用ai接口
        // ai次数计数
        userService.CountAiFrequency(loginUser);
        String rawData = aiManager.sendMesToAI(aiQuestion);
        // 处理数据 TODO 正则获取数据
        String[] splits = StringUtils.split(rawData, "【【【【【");
        ThrowUtils.throwIf(splits.length < 2, ErrorCode.AI_GEN_ERROR);
        String code = splits[0].trim();
        String analyse = splits[1].trim();
        // todo ai生成结果校验
        try {
            ThrowUtils.throwIf(StringUtils.isBlank(analyse), ErrorCode.AI_GEN_ERROR, "ai生成结论异常");
            ThrowUtils.throwIf(StringUtils.isBlank(code), ErrorCode.AI_GEN_ERROR, "ai生成代码异常");
        } catch (BusinessException e) {
            return ResultUtils.error(ErrorCode.AI_GEN_ERROR);
        }

        /**
         * 存入数据库
         */
        Chart Chart = new Chart();
        Chart.setGoal(goal);
        Chart.setName(name);
        Chart.setChartType(chartType);
        Chart.setGenResult(analyse);
        Chart.setUserId(loginUser.getId());
        // 存储基本信息
        boolean result = ChartService.save(Chart);
        // 获得图表id
        Long chartId = Chart.getId();
        // 生成图表代码存储至图表核心数据类
        ChartCoreData coreData = new ChartCoreData();
        coreData.setChartData(csv);
        coreData.setGenChart(code);
        coreData.setChartId(chartId);
        boolean coreSave = chartCoreDataService.save(coreData);
        // todo 保存失败
        ThrowUtils.throwIf(!result || !coreSave, ErrorCode.OPERATION_ERROR, "图表信息保存失败");
        /**
         * 返回生成图表信息
         */
        BiResponse biResponse = new BiResponse();
        biResponse.setId(chartId);
        biResponse.setGenChart(code);
        biResponse.setGenResult(analyse);
        return ResultUtils.success(biResponse);
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/gen/async")
    public BaseResponse<BiResponse> genChartByAiAsync(@RequestPart("file") MultipartFile multipartFile, @Valid @NotNull @ModelAttribute ChartAddRequest chartAddRequest, @NotNull HttpServletRequest request) throws Exception {

        /**
         * 用户登录校验
         */
        User loginUser = userService.getLoginUser(request);

        /**
         * 限流校验
         */
        redissonManager.doReteLimiter("genChart_" + loginUser.getUserAccount());

        log.info("genChart_{}获得执行权限", loginUser.getUserAccount());

        /**
         * 构造ai问答
         */
        String goal = chartAddRequest.getGoal();
        String name = chartAddRequest.getName();
        String chartType = chartAddRequest.getChartType();
        String csv = FileUtils.getFileString(multipartFile);
        String aiQuestion = ChartService.getAiQuestion(csv, goal, chartType);

        /**
         * 不论任务是否开始处理，均先存入数据库，且更改状态
         */
        Chart Chart = new Chart();
        Chart.setGoal(goal);
        Chart.setName(name);
        Chart.setChartType(chartType);
        Chart.setUserId(loginUser.getId());
        Chart.setChartStatus(ChartConstant.WAITING);
        boolean result = ChartService.save(Chart);

        // 生成图表代码存储至图表核心数据类
        ChartCoreData coreData = new ChartCoreData();
        coreData.setChartData(csv);

        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 获得该图表id
        Long chartId = Chart.getId();
        /**
         * 异步化处理ai调用
         */
        CompletableFuture.runAsync(() -> {
            Chart chartRunning = new Chart();
            // 任务处理中，修改状态
            chartRunning.setId(chartId);
            chartRunning.setChartStatus(ChartConstant.RUNNING);
            boolean running = ChartService.updateById(chartRunning);
            if (!running) {
                ChartService.handleGenChartError(chartId, "更新图表running状态失败");
            }
            // 调用ai接口
            // ai次数计数
            userService.CountAiFrequency(loginUser);
            String rawData = aiManager.sendMesToAI(aiQuestion);
            // 处理数据 TODO 正则清洗数据
            String[] splits = StringUtils.split(rawData, "【【【【【");
            // 生成数据校验
            if (splits.length < 2 || StringUtils.isBlank(splits[0]) || StringUtils.isBlank(splits[1])) {
                ChartService.handleGenChartError(chartId, "ai生成数据有误");
            }
            String code = splits[0].trim();
            String analyse = splits[1].trim();

            /**
             * 生成图表存储
             */
            // 生成图表代码存储至图表核心数据类
            ChartCoreData finishCoreData = new ChartCoreData();
            finishCoreData.setChartId(chartId);
            finishCoreData.setChartData(csv);
            finishCoreData.setGenChart(code);
            boolean coreSave = chartCoreDataService.save(finishCoreData);
            // 保存生成结论，修改生成成功更改状态
            Chart successChart = new Chart();
            successChart.setGenResult(analyse);
            successChart.setChartStatus(ChartConstant.SUCCEED);
            successChart.setId(chartId);
            boolean succeed = ChartService.updateById(successChart);
            // 保存失败
            if (!succeed || !coreSave) {
                ChartService.handleGenChartError(chartId, "更新图表succeed状态失败");
            }
        }, threadPoolExecutor);
        /**
         * 异步方法直接返回生成图表id
         */
        BiResponse biResponse = new BiResponse();
        biResponse.setId(chartId);
        return ResultUtils.success(biResponse);
    }


    // todo 参数非空非空格校验失败 后端添加字段查询用户总共调接口次数

    @CrossOrigin(origins = "*")
    @PostMapping("/gen/async/mq")
    public BaseResponse<BiResponse> genChartByAiMQ(@RequestPart("file") MultipartFile multipartFile, @Valid @NotNull @ModelAttribute ChartAddRequest chartAddRequest, @NotNull HttpServletRequest request) {

        /**
         * 用户登录校验
         */
        User loginUser = userService.getLoginUser(request);

        /**
         * 限流校验
         */
        redissonManager.doReteLimiter("genChart_" + loginUser.getUserAccount());

        log.info("genChart_{}获得执行权限", loginUser.getUserAccount());

        String goal = chartAddRequest.getGoal();
        String name = chartAddRequest.getName();
        String chartType = chartAddRequest.getChartType();
        String csv = FileUtils.getFileString(multipartFile);

        /**
         * 不论任务是否开始处理，先讲未处理过的数据存入数据库，且更改状态
         */
        Chart Chart = new Chart();
        Chart.setGoal(goal);
        Chart.setName(name);
        Chart.setChartType(chartType);
        Chart.setUserId(loginUser.getId());
        Chart.setChartStatus(ChartConstant.WAITING);
        boolean result = ChartService.save(Chart);
        // 获得该图表id
        Long chartId = Chart.getId();

        // 图表存储至图表核心数据类
        ChartCoreData coreData = new ChartCoreData();
        coreData.setChartData(csv);
        coreData.setChartId(chartId);
        boolean coreSave = chartCoreDataService.save(coreData);
        ThrowUtils.throwIf(!result || !coreSave, ErrorCode.OPERATION_ERROR);
        /**
         * 使用消息队列保存消息
         */
        biMqProducer.sendMessage(String.valueOf(chartId));
        /**
         * 返回图表id
         */
        BiResponse biResponse = new BiResponse();
        biResponse.setId(chartId);
        return ResultUtils.success(biResponse);
    }


    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param ChartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ChartVO>> listMyChartVOByPage(@Valid @RequestBody ChartQueryRequest ChartQueryRequest,
                                                           @NotNull HttpServletRequest request) {
        // 校验是否登录
        User loginUser = userService.getLoginUser(request);

        ChartQueryRequest.setUserId(loginUser.getId());
        long current = ChartQueryRequest.getCurrent();
        long size = ChartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> ChartPage = ChartService.page(new Page<>(current, size),
                getQueryWrapper(ChartQueryRequest));
        // 从chartCoreData中获取数据传回chart实体类中
        for (Chart chart : ChartPage.getRecords()) {
            Long chartId = chart.getId();
            ChartCoreData coreData = chartCoreDataService.getOne(getQueryCoreWrapper(chartId));
            chart.setGenChart(coreData.getGenChart());
            chart.setChartData(coreData.getChartData());
        }
        return ResultUtils.success(ChartService.getChartVOPage(ChartPage));
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@Valid @NotNull @RequestBody DeleteRequest deleteRequest, @NotNull HttpServletRequest request) throws RuntimeException {

        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = ChartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = ChartService.removeById(id);
        chartCoreDataService.removeById(id);
        return ResultUtils.success(b);
    }

    // 增

    /**
     * 创建
     *
     * @param chartUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @Deprecated
    public BaseResponse<Long> updateChart(@Valid @NotNull @RequestBody ChartUpdateRequest chartUpdateRequest, @NotNull HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);

        Chart Chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, Chart);

        Chart.setUserId(loginUser.getId());

        boolean result = ChartService.save(Chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = Chart.getId();
        return ResultUtils.success(newChartId);
    }


    /**
     * 根据 图表id 获取图表信息
     *
     * @param id 图表Id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ChartVO> getChartVOById(@Min(0) long id, @NotNull HttpServletRequest request) {

        // 校验是否登录
        userService.getLoginUser(request);

        Chart Chart = ChartService.getById(id);
        if (Chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        ChartCoreData chartCoreData = chartCoreDataService.getOne(getQueryCoreWrapper(id));
        // 存入chart中
        Chart.setGenChart(chartCoreData.getGenChart());
        Chart.setChartData(chartCoreData.getChartData());
        return ResultUtils.success(ChartService.ChartToChartVO(Chart));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param ChartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ChartVO>> listChartVOByPage(@Valid @RequestBody ChartQueryRequest ChartQueryRequest,
                                                         @NotNull HttpServletRequest request) {
        // 校验是否登录
        userService.getLoginUser(request);

        // 先查询redis

        // redis中没有，就查询数据库
        long current = ChartQueryRequest.getCurrent();
        long size = ChartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> ChartPage = ChartService.page(new Page<>(current, size),
                getQueryWrapper(ChartQueryRequest));
        // 从chartCoreData中获取数据传回chart实体类中
        for (Chart chart : ChartPage.getRecords()) {
            Long chartId = chart.getId();
            ChartCoreData coreData = chartCoreDataService.getOne(getQueryCoreWrapper(chartId));
            chart.setGenChart(coreData.getGenChart());
            chart.setChartData(coreData.getChartData());
        }

        // 数据库中查询结束之后存入redis里
        return ResultUtils.success(ChartService.getChartVOPage(ChartPage));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Chart>> listMyChartByPage(@NotNull @Valid @RequestBody ChartQueryRequest ChartQueryRequest,
                                                       @NotNull HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ChartQueryRequest.setUserId(loginUser.getId());
        long current = ChartQueryRequest.getCurrent();
        long size = ChartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> ChartPage = ChartService.page(new Page<>(current, size),
                getQueryWrapper(ChartQueryRequest));
        // 从chartCoreData中获取数据传回chart实体类中
        for (Chart chart : ChartPage.getRecords()) {
            Long chartId = chart.getId();
            ChartCoreData coreData = chartCoreDataService.getOne(getQueryCoreWrapper(chartId));
            chart.setGenChart(coreData.getGenChart());
            chart.setChartData(coreData.getChartData());
        }
        return ResultUtils.success(ChartPage);
    }

    /**
     * queryWrapper构造查询chart
     *
     * @param
     * @return
     */
    private QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest ChartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (ChartQueryRequest == null) {
            return queryWrapper;
        }
        Long id = ChartQueryRequest.getId();
        String name = ChartQueryRequest.getName();
        String goal = ChartQueryRequest.getGoal();
        String chartType = ChartQueryRequest.getChartType();
        Long userId = ChartQueryRequest.getUserId();
        String sortField = ChartQueryRequest.getSortField();
        String sortOrder = ChartQueryRequest.getSortOrder();

        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * queryWrapper构造根据chartId查询chart核心数据
     *
     * @param chartId
     * @return
     */
    private QueryWrapper<ChartCoreData> getQueryCoreWrapper(Long chartId) {
        QueryWrapper<ChartCoreData> queryCoreWrapper = new QueryWrapper<>();
        if (chartId == null) {
            return queryCoreWrapper;
        }
        queryCoreWrapper.eq("chartId", chartId);
        return queryCoreWrapper;
    }

    /**
     * 创建
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
//    @PostMapping("/add")
    @Deprecated
    public BaseResponse<Long> addChart(@Valid @NotNull @RequestBody ChartAddRequest chartAddRequest, @NotNull HttpServletRequest request) {

        Chart Chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, Chart);

        User loginUser = userService.getLoginUser(request);
        Chart.setUserId(loginUser.getId());

        boolean result = ChartService.save(Chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = Chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 分页获取不限本用户列表（仅管理员）
     *
     * @param ChartQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    @PostMapping("/list/page")
    @Deprecated
    public BaseResponse<Page<Chart>> listChartByPage(@Valid @RequestBody ChartQueryRequest ChartQueryRequest, @NotNull HttpServletRequest request) {

        // 校验是否登录
        userService.getLoginUser(request);

        long current = ChartQueryRequest.getCurrent();
        long size = ChartQueryRequest.getPageSize();
        Page<Chart> ChartPage = ChartService.page(new Page<>(current, size),
                getQueryWrapper(ChartQueryRequest));
        // 从chartCoreData中获取数据传回chart实体类中
        for (Chart chart : ChartPage.getRecords()) {
            Long chartId = chart.getId();
            ChartCoreData chartCoreData = chartCoreDataService.getOne(getQueryCoreWrapper(chartId));
            // 存入chart中
            chart.setGenChart(chartCoreData.getGenChart());
            chart.setChartData(chartCoreData.getChartData());
        }
        return ResultUtils.success(ChartPage);
    }


}