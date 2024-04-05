package com.lilyVia.springbootinit.model.dto.chart;

import java.io.Serializable;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 编辑请求
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
@Data
public class ChartEditRequest implements Serializable {

    /**
     * 分析目标
     */
    @NotBlank
    @NotEmpty
    @Length(max = 1024) // 不能超过1000字符，500字
    private String goal;

    /**
     * 图表名称
     */
    @NotBlank
    @NotEmpty
    @Length(max = 32)
    private String name;

    /**
     * 生成图表的类型
     */
    @NotBlank
    @NotEmpty
    private String chartType;

    /**
     * 目标图表数据
     */
    @NotBlank
    @NotEmpty
    private String chartData;;


    private static final long serialVersionUID = 1L;
}