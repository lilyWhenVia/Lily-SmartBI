package com.lilyVia.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lilyVia.springbootinit.model.dto.chart.ChartQueryRequest;
import com.lilyVia.springbootinit.model.entity.Chart;
import com.lilyVia.springbootinit.model.vo.ChartVO;
import com.lilyVia.springbootinit.model.vo.UserVO;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;

/**
* @author 22825
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-03-12 10:34:35
*/
public interface ChartService extends IService<Chart> {

    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

    Page<ChartVO> getChartVOPage(Page<Chart> chartPage);

    void handleGenChartError(long chartId, String message);

    String getAiQuestion(String csv, String goal, String chartType);


    void saveChartResult(long chartId, String code, String analyse);

    ChartVO ChartToChartVO(Chart Chart);
}
