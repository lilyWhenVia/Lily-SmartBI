package com.lilyVia.springbootinit.service;

import com.lilyVia.springbootinit.model.entity.Chart;
import com.lilyVia.springbootinit.model.entity.ChartCoreData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Created by lily via on 2024/4/5 14:35
 */
@SpringBootTest
class ChartCoreDataServiceTest {

    @Resource
    private ChartService chartService;

    @Resource
    private ChartCoreDataService chartCoreDataService;

    private Long current = 1L;
    private Long size = 7L;

    @Test
    public void chartCoreDataTest(){
        /**
         * 存入数据库
         */
        Chart Chart = new Chart();
        Chart.setGoal("goal");
        Chart.setName("name");
        Chart.setChartType("chartType");
        Chart.setGenResult("analyse");
        Chart.setUserId(1775508948422594562L);
        // 存储基本信息
        boolean result = chartService.save(Chart);
        // 获得图表id
        Long chartId = Chart.getId();
        // 生成图表代码存储至图表核心数据类
        ChartCoreData coreData = new ChartCoreData();
        coreData.setChartData("csv");
        coreData.setGenChart("code");
        coreData.setChartId(chartId);
        boolean coreSave = chartCoreDataService.save(coreData);
    }

//    public void pageTest(){
//        age<Chart> ChartPage = ChartService.page(new Page<>(current, size),
//                getQueryWrapper(ChartQueryRequest));
//        Page<ChartCoreData> chartCorePage = chartCoreDataService.page(new Page<>(current, size),
//                getQueryCoreWrapper(ChartQueryRequest));
//        for (ChartCoreData coreData : chartCorePage.getRecords()) {
//            for (Chart chart : ChartPage.getRecords()) {
//                if (coreData.getChartId().equals(chart.getId())){
//                    chart.setChartData(coreData.getChartData());
//                    chart.setGenChart(coreData.getGenChart());
//                }
//            }
//        }
//    }
//
//    private QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest ChartQueryRequest) {
//        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
//        if (ChartQueryRequest == null) {
//            return queryWrapper;
//        }
//        Long id = ChartQueryRequest.getId();
//        String name = ChartQueryRequest.getName();
//        String goal = ChartQueryRequest.getGoal();
//        String chartType = ChartQueryRequest.getChartType();
//        Long userId = ChartQueryRequest.getUserId();
//        String sortField = ChartQueryRequest.getSortField();
//        String sortOrder = ChartQueryRequest.getSortOrder();
//
//        queryWrapper.eq(id != null && id > 0, "id", id);
//        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
//        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
//        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
//        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
//        queryWrapper.eq("isDelete", false);
//        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
//                sortField);
//        return queryWrapper;
//    }
//
//
//    private QueryWrapper<ChartCoreData> getQueryCoreWrapper(ChartQueryRequest ChartQueryRequest) {
//        QueryWrapper<ChartCoreData> queryCoreWrapper = new QueryWrapper<>();
//        if (ChartQueryRequest == null) {
//            return queryCoreWrapper;
//        }
//        Long id = ChartQueryRequest.getId();
//        String name = ChartQueryRequest.getName();
//        String goal = ChartQueryRequest.getGoal();
//        String chartType = ChartQueryRequest.getChartType();
//        Long userId = ChartQueryRequest.getUserId();
//        String sortField = ChartQueryRequest.getSortField();
//        String sortOrder = ChartQueryRequest.getSortOrder();
//
//        queryCoreWrapper.eq(id != null && id > 0, "id", id);
//        queryCoreWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
//                sortField);
//        return queryCoreWrapper;
//    }


}