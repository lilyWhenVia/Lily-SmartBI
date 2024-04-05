package com.lilyVia.springbootinit.model.dto.chart;

import com.lilyVia.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 查询请求
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户
     */
    private long id;

    /**
     * 分析目标
     */

    private String goal;

    /**
     * 图表名称
     */

    private String name;

    /**
     * 生成图表的类型
     */

    private String chartType;

    /**
     * 目标图表数据
     */
    private String chartData;

    private Long userId;

    private Long current;

    private Long pageSize;

    private String sortField;

    private String sortOrder;

    private static final long serialVersionUID = 1L;
}