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
import com.lilyVia.springbootinit.model.entity.Chart;
import com.lilyVia.springbootinit.model.entity.User;
import com.lilyVia.springbootinit.model.vo.ChartVO;
import com.lilyVia.springbootinit.rabbitMq.BiMqProducer;
import com.lilyVia.springbootinit.service.ChartService;
import com.lilyVia.springbootinit.service.UserService;
import com.lilyVia.springbootinit.utils.FileUtils;
import com.lilyVia.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    private UserService userService;

    @Resource
    private AIManager aiManager;

    @Resource
    private RedissonManager redissonManager;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private BiMqProducer biMqProducer;

    /**
     * 创建
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@Valid @NotNull @RequestBody ChartAddRequest chartAddRequest,@NotNull  HttpServletRequest request) {

        Chart Chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, Chart);

        User loginUser = userService.getLoginUser(request);
        Chart.setUserId(loginUser.getId());

        boolean result = ChartService.save(Chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = Chart.getId();
        return ResultUtils.success(newChartId);
    }


    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,@Valid @NotNull @ModelAttribute ChartAddRequest chartAddRequest, @NotNull HttpServletRequest request) {

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
        String rawData = aiManager.sendMesToAIUseXingHuo(aiQuestion);
        // 处理数据 TODO 正则获取数据
        String[] splits = StringUtils.split(rawData, "【【【【【");
        ThrowUtils.throwIf(splits.length < 2, ErrorCode.AI_GEN_ERROR);
        String code = splits[0].trim();
        String analyse = splits[1].trim();
        // todo ai生成结果校验
        ThrowUtils.throwIf(StringUtils.isBlank(code), ErrorCode.AI_GEN_ERROR, "ai生成代码异常");
        ThrowUtils.throwIf(StringUtils.isBlank(analyse), ErrorCode.AI_GEN_ERROR, "ai生成结论异常");

        /**
         * 存入数据库
         */
        Chart Chart = new Chart();
        Chart.setGoal(goal);
        Chart.setName(name);
        Chart.setChartData(csv);
        Chart.setChartType(chartType);
        // 生成图表代码存储至
        Chart.setGenChart(code);
        Chart.setGenResult(analyse);
        Chart.setUserId(loginUser.getId());
        boolean result = ChartService.save(Chart);
        // 保存失败
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "图表信息保存失败");
        /**
         * 返回生成图表信息
         */
        Long chartId = Chart.getId();
        BiResponse biResponse = new BiResponse();
        biResponse.setId(chartId);
        biResponse.setGenChart(code);
        biResponse.setGenResult(analyse);
        return ResultUtils.success(biResponse);
    }


    @PostMapping("/gen/async")
    public BaseResponse<BiResponse> genChartByAiAsync(@RequestPart("file") MultipartFile multipartFile,@Valid @NotNull @ModelAttribute ChartAddRequest chartAddRequest, @NotNull HttpServletRequest request) throws Exception {

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
        Chart.setChartData(csv);
        Chart.setChartType(chartType);
        Chart.setUserId(loginUser.getId());
        Chart.setChartStatus(ChartConstant.WAITING);
        boolean result = ChartService.save(Chart);
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
            String rawData = aiManager.sendMesToAIUseXingHuo(aiQuestion);
            // 处理数据 TODO 正则清洗数据
            String[] splits = StringUtils.split(rawData, "【【【【【");
            // 生成数据校验
            if (splits.length < 2 || StringUtils.isBlank(splits[0]) || StringUtils.isBlank(splits[1])) {
                ChartService.handleGenChartError(chartId, "ai生成数据有误");
            }
            String code = splits[0].trim();
            String analyse = splits[1].trim();

            // 生成图表代码存储
            Chart.setGenChart(code);
            Chart.setGenResult(analyse);
            Chart.setUserId(loginUser.getId());
            // 生成成功更改状态
            Chart.setChartStatus(ChartConstant.SUCCEED);
            boolean succeed = ChartService.save(Chart);
            // 保存失败
            if (!succeed) {
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
    @PostMapping("/gen/async/mq")
    public BaseResponse<BiResponse> genChartByAiMQ(@RequestPart("file") MultipartFile multipartFile, @Valid @NotNull @ModelAttribute ChartAddRequest chartAddRequest,@NotNull  HttpServletRequest request) {

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
        Chart.setChartData(csv);
        Chart.setChartType(chartType);
        Chart.setUserId(loginUser.getId());
        Chart.setChartStatus(ChartConstant.WAITING);
        boolean result = ChartService.save(Chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 获得该图表id
        Long chartId = Chart.getId();
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
     * 处理图表异常状态通用方法
     * @param chartId 图表id
     * @param message 异常错误信息
     */


    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
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
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param ChartUpdateRequest
     * @return
     */

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ChartVO> getChartVOById(@NotNull long id, @NotNull HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart Chart = ChartService.getById(id);
        if (Chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(ChartService.getChartVO(Chart, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param ChartQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(@Valid @RequestBody ChartQueryRequest ChartQueryRequest) {
        long current = ChartQueryRequest.getCurrent();
        long size = ChartQueryRequest.getPageSize();
        Page<Chart> ChartPage = ChartService.page(new Page<>(current, size),
                getQueryWrapper(ChartQueryRequest));
        return ResultUtils.success(ChartPage);
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
        long current = ChartQueryRequest.getCurrent();
        long size =  ChartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> ChartPage = ChartService.page(new Page<>(current, size),
                getQueryWrapper(ChartQueryRequest));
        return ResultUtils.success(ChartService.getChartVOPage(ChartPage, request));
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

        if (ChartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ChartQueryRequest.setUserId(loginUser.getId());        
        long current = ChartQueryRequest.getCurrent();
        long size =  ChartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> ChartPage = ChartService.page(new Page<>(current, size),
                getQueryWrapper(ChartQueryRequest));
        return ResultUtils.success(ChartService.getChartVOPage(ChartPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Chart>> listMyChartByPage(@Valid @RequestBody ChartQueryRequest ChartQueryRequest,
                                                       @NotNull  HttpServletRequest request) {
        if (ChartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ChartQueryRequest.setUserId(loginUser.getId());
        long current = ChartQueryRequest.getCurrent();
        long size =  ChartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = ChartService.page(new Page<>(current, size),
                getQueryWrapper(ChartQueryRequest));
        return ResultUtils.success(chartPage);
    }


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



}