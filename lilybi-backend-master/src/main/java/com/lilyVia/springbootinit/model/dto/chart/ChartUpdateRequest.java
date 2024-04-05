package com.lilyVia.springbootinit.model.dto.chart;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class ChartUpdateRequest implements Serializable {

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
     * 目标图表数据
     */
    private String chartData;


    private static final long serialVersionUID = 1L;
}