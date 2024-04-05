package com.lilyVia.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilyVia.springbootinit.common.BaseResponse;
import com.lilyVia.springbootinit.common.DeleteRequest;
import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.common.ResultUtils;
import com.lilyVia.springbootinit.constant.CommonConstant;
import com.lilyVia.springbootinit.exception.BusinessException;
import com.lilyVia.springbootinit.exception.ThrowUtils;
import com.lilyVia.springbootinit.model.dto.question.QuestionQueryRequest;
import com.lilyVia.springbootinit.model.entity.Question;
import com.lilyVia.springbootinit.model.entity.Question;
import com.lilyVia.springbootinit.model.entity.User;
import com.lilyVia.springbootinit.model.vo.UserVO;
import com.lilyVia.springbootinit.service.QuestionService;
import com.lilyVia.springbootinit.service.QuestionService;
import com.lilyVia.springbootinit.service.UserService;
import com.lilyVia.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by lily via on 2024/4/6 3:48
 */

@RestController
@RequestMapping("/aiFrequency")
@Slf4j
public class AiFrequencyController {

    @Resource
    private UserService userService;

    @Resource
    private QuestionService QuestionService;


    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@Valid @NotNull @RequestBody DeleteRequest deleteRequest, @NotNull HttpServletRequest request) throws RuntimeException {

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

    @GetMapping("/get")
    public BaseResponse<UserVO> getAiFrequency(HttpServletRequest request){
        /**
         * 用户登录校验
         */
        User loginUser = userService.getLoginUser(request);
        UserVO userVO = new UserVO();
        userVO.setId(loginUser.getId());
        userVO.setRemainFrequency(loginUser.getRemainFrequency());
        userVO.setTotalFrequency(loginUser.getTotalFrequency());
        return ResultUtils.success(userVO);
    }


    @PostMapping("/my/list/page")
    public BaseResponse<Page<Question>> listMyQuestionByPage(@Valid @RequestBody QuestionQueryRequest QuestionQueryRequest,
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
