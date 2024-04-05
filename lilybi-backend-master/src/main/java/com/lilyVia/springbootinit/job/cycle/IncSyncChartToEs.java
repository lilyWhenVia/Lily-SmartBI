package com.lilyVia.springbootinit.job.cycle;

import com.lilyVia.springbootinit.mapper.ChartMapper;
import com.lilyVia.springbootinit.service.ChartService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 增量同步帖子到 es
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class IncSyncChartToEs {

    @Resource
    private ChartMapper chartMapper;

    @Resource
    private ChartService chartService;

//    /**
//     * 每分钟执行一次
//     */
//    @Scheduled(fixedRate = 60 * 1000)
//    public void run() {
//        // 查询近 5 分钟内的数据
//        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
//        List<Chart> chartList = chartMapper.(fiveMinutesAgoDate);
//        if (CollUtil.isEmpty(chartList)) {
//            log.info("no inc chart");
//            return;
//        }
//        List<Chart> chartEsDTOList = chartList.stream()
//                .map()
//                .collect(Collectors.toList());
//        final int pageSize = 500;
//        int total = chartEsDTOList.size();
//        log.info("IncSyncChartToEs start, total {}", total);
//        for (int i = 0; i < total; i += pageSize) {
//            int end = Math.min(i + pageSize, total);
//            log.info("sync from {} to {}", i, end);
//        }
//        log.info("IncSyncChartToEs end, total {}", total);
//    }
}
