package com.lilyVia.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 图表核心数据表
 * @TableName chart_core_data
 */
@TableName(value ="chart_core_data")
@Data
public class ChartCoreData implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 图表信息表的id
     */
    private Long chartId;

    /**
     * 生成的图表
     */
    private String genChart;

    /**
     * 目标图表数据
     */
    private String chartData;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}