package com.lilyVia.springbootinit.model.dto.question;

import com.lilyVia.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户
     */
    private long id;

    /**
     * 分析目标
     */

    private String questionGoal;

    /**
     * 图表名称
     */

    private String questionName;

    /**
     * 生成图表的类型
     */

    private String questionType;


    private Long userId;

    private Long current;

    private Long pageSize;

    private String sortField;

    private String sortOrder;

    private static final long serialVersionUID = 1L;
}