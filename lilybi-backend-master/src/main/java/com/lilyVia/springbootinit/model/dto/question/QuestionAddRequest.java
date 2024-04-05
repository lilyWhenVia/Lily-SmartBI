package com.lilyVia.springbootinit.model.dto.question;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 创建请求
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 分析目标
     */
    @NotBlank
    @NotEmpty
    @Length(max = 1024) // 不能超过1000字符，500字
    private String questionGoal;

    /**
     * 图表名称
     */
    @NotBlank
    @NotEmpty
    @Length(max = 32)
    private String questionName;

    /**
     * 生成图表的类型
     */
    @NotBlank
    @NotEmpty
    private String questionType;


    private String questionResult;



    private static final long serialVersionUID = 1L;
}