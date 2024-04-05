package com.lilyVia.springbootinit.model.vo;

import com.lilyVia.springbootinit.model.entity.Chart;
import com.lilyVia.springbootinit.model.entity.ChartCoreData;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 图表视图类
 * @author Lily Via
 * @date 2024/4/1 17:43
 * @version 1.0
 */
@Data
public class ChartVO implements Serializable {

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表名称
     */
    private String name;

    /**
     * 图表状态：决定是否展示图表
     */
    private Integer chartStatus;


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
    private UserVO userVO;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 包装类转对象
     *
     * @param ChartVO
     * @return
     */
    public static Chart voToObj(ChartVO ChartVO) {
        if (ChartVO == null) {
            return null;
        }
        Chart Chart = new Chart();
        BeanUtils.copyProperties(ChartVO, Chart);
        return Chart;
    }

    /**
     * 对象转包装类
     *
     * @param Chart
     * @return
     */
    public static ChartVO objToVo(Chart Chart) {
        if (Chart == null) {
            return null;
        }
        ChartVO ChartVO = new ChartVO();
        BeanUtils.copyProperties(Chart, ChartVO);
        return ChartVO;
    }

}
