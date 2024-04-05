package com.lilyVia.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ai问答信息表
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * ai问答处理状态信息
     */
    private String executorMessage;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 分析目标
     */
    private String questionGoal;

    /**
     * ai问答名称
     */
    private String questionName;

    /**
     * 目标ai问答数据
     */
    private String questionResult;

    /**
     * ai问答处理状态
     */
    private Integer questionStatus;

    /**
     * 生成ai问答的类型
     */
    private String questionType;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户 id
     */
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}