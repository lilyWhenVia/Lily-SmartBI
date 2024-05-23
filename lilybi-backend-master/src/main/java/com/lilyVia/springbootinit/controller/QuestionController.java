package com.lilyVia.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilyVia.springbootinit.annotation.AuthCheck;
import com.lilyVia.springbootinit.common.*;
import com.lilyVia.springbootinit.constant.CommonConstant;
import com.lilyVia.springbootinit.constant.QuestionConstant;
import com.lilyVia.springbootinit.constant.UserConstant;
import com.lilyVia.springbootinit.exception.BusinessException;
import com.lilyVia.springbootinit.exception.ThrowUtils;
import com.lilyVia.springbootinit.manager.AIManager;
import com.lilyVia.springbootinit.manager.RedissonManager;
import com.lilyVia.springbootinit.model.dto.question.QuestionAddRequest;
import com.lilyVia.springbootinit.model.dto.question.QuestionQueryRequest;
import com.lilyVia.springbootinit.model.entity.Question;
import com.lilyVia.springbootinit.model.entity.User;
import com.lilyVia.springbootinit.model.vo.QuestionVO;
import com.lilyVia.springbootinit.rabbitMq.BiMqProducer;
import com.lilyVia.springbootinit.service.QuestionService;
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
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 图表接口
 */
@RestController
@RequestMapping("/aiAssistant")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService QuestionService;

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
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@Valid @NotNull @RequestBody QuestionAddRequest questionAddRequest,@NotNull  HttpServletRequest request) {

        Question Question = new Question();
        BeanUtils.copyProperties(questionAddRequest, Question);

        User loginUser = userService.getLoginUser(request);
        Question.setUserId(loginUser.getId());

        boolean result = QuestionService.save(Question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = Question.getId();
        return ResultUtils.success(newQuestionId);
    }


   

    @PostMapping("/chat")
    public BaseResponse<BiResponse> genQuestionByAiAsync(@RequestBody QuestionAddRequest questionAddRequest, @NotNull HttpServletRequest request) throws Exception {

        /**
         * 用户登录校验
         */
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        /**
         * 限流校验
         */
        redissonManager.doReteLimiter("genQuestion_" + loginUser.getUserAccount());

        log.info("genQuestion_{}获得执行权限", loginUser.getUserAccount());

        /**
         * 构造ai问答
         */
        String goal = questionAddRequest.getQuestionGoal();
        String name = questionAddRequest.getQuestionName();
        String questionType = questionAddRequest.getQuestionType();
        String aiQuestion = goal+name+"问题类型："+questionType;

        /**
         * 不论任务是否开始处理，均先存入数据库，且更改状态
         */
        Question Question = new Question();
        Question.setQuestionGoal(goal);
        Question.setQuestionName(name);
        Question.setQuestionType(questionType);
        Question.setUserId(loginUser.getId());
        Question.setQuestionStatus(QuestionConstant.WAITING);

        boolean result = QuestionService.save(Question);
        Long questionId = Question.getId();
        if (!result){
            QuestionService.handleGenChartError(questionId, "更新等待状态失败");
        }
        // 获得该图表id
        /**
         * 异步化处理ai调用
         */
        CompletableFuture.runAsync(() -> {
            Question questionRunning = new Question();
            // 任务处理中，修改状态
            questionRunning.setId(questionId);
            questionRunning.setQuestionStatus(QuestionConstant.RUNNING);
            @AssertTrue
            boolean running = QuestionService.updateById(questionRunning);
            if (!running){
                QuestionService.handleGenChartError(questionId, "更新运行状态失败");
            }
            // 调用ai接口
            // ai次数计数
            userService.CountAiFrequency(loginUser);
            String questionResult = aiManager.chatWithAi(aiQuestion);
            User user = userService.getById(userId);

            // 生成结果存储
            Question questionSucceed = new Question();
            questionSucceed.setQuestionResult(questionResult);
            questionSucceed.setUserId(userId);
            // 生成成功更改状态
            questionSucceed.setQuestionStatus(QuestionConstant.SUCCEED);
            boolean succeed = QuestionService.save(questionSucceed);
            // 保存失败

        }, threadPoolExecutor);
        /**
         * 异步方法直接返回生成图表id
         */
        BiResponse biResponse = new BiResponse();
        biResponse.setId(questionId);
        return ResultUtils.success(biResponse);
    }
    
    /**
     * 处理图表异常状态通用方法
     * @param questionId 图表id
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
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = QuestionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = QuestionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param QuestionUpdateRequest
     * @return
     */

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<QuestionVO> getQuestionVOById(@NotNull long id, @NotNull HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question Question = QuestionService.getById(id);
        if (Question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(Question, questionVO);
        return ResultUtils.success(questionVO);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param QuestionQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionByPage(@Valid @RequestBody QuestionQueryRequest QuestionQueryRequest) {
        long current = QuestionQueryRequest.getCurrent();
        long size = QuestionQueryRequest.getPageSize();
        Page<Question> QuestionPage = QuestionService.page(new Page<>(current, size),
                getQueryWrapper(QuestionQueryRequest));
        return ResultUtils.success(QuestionPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param QuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@Valid @RequestBody QuestionQueryRequest QuestionQueryRequest,
                                                         @NotNull HttpServletRequest request) {
        long current = QuestionQueryRequest.getCurrent();
        long size =  QuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> QuestionPage = QuestionService.page(new Page<>(current, size),
                getQueryWrapper(QuestionQueryRequest));
        List<Question> questionList = QuestionPage.getRecords();
        ArrayList<QuestionVO> questionVOList = new ArrayList<>();
        for (Question question : questionList) {
            QuestionVO vo = new QuestionVO();
            BeanUtils.copyProperties(question, vo);
            questionVOList.add(vo);
        }
        Page<QuestionVO> voPage = new Page<>();
        BeanUtils.copyProperties(QuestionPage, voPage);
        voPage.setRecords(questionVOList);
        return ResultUtils.success(voPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param QuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@Valid @RequestBody QuestionQueryRequest QuestionQueryRequest,
                                                           @NotNull HttpServletRequest request) {

        if (QuestionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QuestionQueryRequest.setUserId(loginUser.getId());        
        long current = QuestionQueryRequest.getCurrent();
        long size =  QuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> QuestionPage = QuestionService.page(new Page<>(current, size),
                getQueryWrapper(QuestionQueryRequest));
        List<Question> questionList = QuestionPage.getRecords();
        ArrayList<QuestionVO> questionVOList = new ArrayList<>();
        for (Question question : questionList) {
            QuestionVO vo = new QuestionVO();
            BeanUtils.copyProperties(question, vo);
            questionVOList.add(vo);
        }
        Page<QuestionVO> voPage = new Page<>();
        BeanUtils.copyProperties(QuestionPage, voPage);
        voPage.setRecords(questionVOList);
        return ResultUtils.success(voPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Question>> listMyQuestionByPage(@Valid @RequestBody QuestionQueryRequest QuestionQueryRequest,
                                                       @NotNull  HttpServletRequest request) {
        if (QuestionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QuestionQueryRequest.setUserId(loginUser.getId());
        long current = QuestionQueryRequest.getCurrent();
        long size =  QuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = QuestionService.page(new Page<>(current, size),
                getQueryWrapper(QuestionQueryRequest));
        return ResultUtils.success(questionPage);
    }


    private QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest QuestionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (QuestionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = QuestionQueryRequest.getId();
        String name = QuestionQueryRequest.getQuestionName();
        String goal = QuestionQueryRequest.getQuestionGoal();
        String questionType = QuestionQueryRequest.getQuestionType();
        Long userId = QuestionQueryRequest.getUserId();
        String sortField = QuestionQueryRequest.getSortField();
        String sortOrder = QuestionQueryRequest.getSortOrder();

        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(questionType), "questionType", questionType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }



}