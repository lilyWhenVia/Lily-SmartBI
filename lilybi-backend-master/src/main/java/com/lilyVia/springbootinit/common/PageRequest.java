package com.lilyVia.springbootinit.common;

import com.lilyVia.springbootinit.constant.CommonConstant;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 分页请求
 *
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    @NotNull
    private Long current = 1L;

    /**
     * 页面大小
     */
    @NotNull
    private Long pageSize = 10L;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
