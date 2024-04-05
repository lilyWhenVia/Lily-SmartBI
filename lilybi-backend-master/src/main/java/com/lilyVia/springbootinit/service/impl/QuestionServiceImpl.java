package com.lilyVia.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.constant.QuestionConstant;
import com.lilyVia.springbootinit.exception.BusinessException;
import com.lilyVia.springbootinit.model.entity.Question;
import com.lilyVia.springbootinit.service.QuestionService;
import com.lilyVia.springbootinit.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author 22825
* @description 针对表【question(ai问答信息表)】的数据库操作Service实现
* @createDate 2024-04-06 02:02:34
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    /**
     * @description: 更新数据库状态异常
     * @param: chartId
    message
     * @return:
     * @author lily via
     * @date: 2024/3/31 3:18
     */
    @Override
    public void handleGenChartError(long questionId, String message) {
        log.error(message);
        Question updateQuestion = new Question();
        updateQuestion.setId(questionId);
        // 重试记录数据库中图表更新状态异常情况
        updateQuestion.setQuestionStatus(QuestionConstant.UPDATE_STATUE_FILED);
        updateQuestion.setExecutorMessage(message);
        boolean b = updateById(updateQuestion);
        if (!b) {
            log.error("更新图表 【失败的状态】 失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新图表 【失败的状态】 失败");
        }
    }

}




