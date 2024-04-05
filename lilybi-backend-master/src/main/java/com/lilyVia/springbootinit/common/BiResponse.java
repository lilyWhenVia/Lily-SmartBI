package com.lilyVia.springbootinit.common;

import lombok.Data;

/**
 * 返回图标生成结果
 * Created by lily via on 2024/3/25 10:19
 */
@Data
public class BiResponse {

    /**
     * 图表在数据库中的主键id
     */
    private Long id;


    /**
     * 生成的图表代码
     */
    private String genChart;

    /**
     * 生成的图表结论
     */
    private String genResult;
}
