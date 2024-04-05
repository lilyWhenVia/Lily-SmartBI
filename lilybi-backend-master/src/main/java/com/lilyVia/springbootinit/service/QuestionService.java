package com.lilyVia.springbootinit.service;

import com.lilyVia.springbootinit.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 22825
* @description 针对表【question(ai问答信息表)】的数据库操作Service
* @createDate 2024-04-06 02:02:34
*/
public interface QuestionService extends IService<Question> {

    void handleGenChartError(long questionId, String message);
}
