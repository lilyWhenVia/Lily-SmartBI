package com.lilyVia.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.constant.ChartConstant;
import com.lilyVia.springbootinit.constant.CommonConstant;
import com.lilyVia.springbootinit.exception.BusinessException;
import com.lilyVia.springbootinit.mapper.ChartMapper;
import com.lilyVia.springbootinit.model.dto.chart.ChartQueryRequest;
import com.lilyVia.springbootinit.model.entity.Chart;
import com.lilyVia.springbootinit.model.entity.ChartCoreData;
import com.lilyVia.springbootinit.model.entity.User;
import com.lilyVia.springbootinit.model.vo.ChartVO;
import com.lilyVia.springbootinit.model.vo.UserVO;
import com.lilyVia.springbootinit.service.ChartCoreDataService;
import com.lilyVia.springbootinit.service.ChartService;
import com.lilyVia.springbootinit.service.UserService;
import com.lilyVia.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 图表服务实现
 */
@Service
@Slf4j
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {

    @Resource
    private ChartCoreDataService chartCoreDataService;

    @Resource
    private UserService userService;


    /**
     * 获取查询包装类
     *
     * @param ChartQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest ChartQueryRequest) {

        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (ChartQueryRequest == null) {
            return queryWrapper;
        }
        String goal = ChartQueryRequest.getGoal();
        String name = ChartQueryRequest.getName();
        String chartData = ChartQueryRequest.getChartData();
        String chartType = ChartQueryRequest.getChartType();
        Long userId = ChartQueryRequest.getUserId();
        String sortField = ChartQueryRequest.getSortField();
        String sortOrder = ChartQueryRequest.getSortOrder();
        // 拼接查询条件
        if (!StringUtils.isAnyBlank(name, goal)) {
            queryWrapper.and(qw -> qw.like("name", name).or().like("goal", goal));
        }
        queryWrapper.like(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.like(StringUtils.isNotBlank(chartData), "chartData", chartData);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 返回chartVO分页查询类
     * @param chartPage
     * @return
     */
    @Override
    public Page<ChartVO> getChartVOPage(Page<Chart> chartPage) {
        List<Chart> charts = chartPage.getRecords();
        List<ChartVO> chartVOS = new ArrayList<>();
        for (Chart chart : charts) {
            ChartVO chartVO = this.ChartToChartVO(chart);
            chartVOS.add(chartVO);
        }
        Page<ChartVO> voPage = new Page<>();
        BeanUtils.copyProperties(chartPage, voPage);
        voPage.setRecords(chartVOS);
        return voPage;
    }



    /**
     * @description: 更新数据库状态异常
            * @param: chartId
                        message
            * @return:
            * @author lily via
            * @date: 2024/3/31 3:18
     */
    @Override
    public void handleGenChartError(long chartId, String message) {
        log.error(message);
        Chart updateChart = new Chart();
        updateChart.setId(chartId);
        // 重试记录数据库中图表更新状态异常情况
        updateChart.setChartStatus(ChartConstant.UPDATE_STATUE_FILED);
        updateChart.setExecutorMessage(message);
        boolean b = updateById(updateChart);
        if (!b) {
            log.error("更新图表 【失败的状态】 失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新图表 【失败的状态】 失败");
        }
    }

    /**
     * 构造ai查询
     *
     * @return ai查询的问题字符串
     */
    @Override
    public String getAiQuestion(String csv, String goal, String chartType) {
        StringBuilder builder = new StringBuilder();
        builder.append("你是一名优秀的数据分析师，根据分析目标:" + goal + ",以及以下数据帮我生成一个" + chartType + "图表。");
        builder.append(csv);
        return builder.toString();
    }

    @Override
    public void saveChartResult(long chartId, String code, String analyse) {
        // 生成图表代码存储至图表核心数据类
        ChartCoreData coreData = new ChartCoreData();
        coreData.setGenChart(code);
        coreData.setChartId(chartId);
        boolean coreSave = chartCoreDataService.update(coreData, new QueryWrapper<ChartCoreData>().eq("chartId", chartId));
        // 生成成功更改状态
        Chart chart = new Chart();
        chart.setId(chartId);
        chart.setGenResult(analyse);
        chart.setChartStatus(ChartConstant.SUCCEED);
        boolean succeed = this.updateById(chart);
        if (!succeed) {
            handleGenChartError(chartId, "更新图表succeed状态失败");
        }
    }

    /**
     *  获得chart包装类（包含userVo）
     * @param chart
     * @return
     */
    @Override
    public ChartVO ChartToChartVO(Chart chart) {
        if (chart == null) {
            return null;
        }
        ChartVO chartVO = new ChartVO();
        User user = userService.getById(chart.getUserId());
        UserVO userVO = userService.getUserVO(user);
        BeanUtils.copyProperties(chart, chartVO);
        chartVO.setUserVO(userVO);
        return chartVO;
    }
}




