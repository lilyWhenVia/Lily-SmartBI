package com.lilyVia.springbootinit.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lilyVia.springbootinit.constant.ChartConstant;
import lombok.Data;

/**
 * 图表信息表
 * @TableName chart
 */
@Data
@TableName("chart")
public class Chart implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务执行情况
     */
    private Integer chartStatus= ChartConstant.WAITING;

    /**
     * 任务执行信息
     */
    private String executorMessage;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表名称
     */
    private String name;

    /**
     * 目标图表数据
     */
    private String chartData;

    /**
     * 生成图表的类型
     */
    private String chartType;

    /**
     * 生成的图表
     */
    private String genChart;

    /**
     * 生成的图表
     */
    private String genResult;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic()
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}