package com.lilyVia.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilyVia.springbootinit.model.entity.ChartCoreData;
import com.lilyVia.springbootinit.service.ChartCoreDataService;
import com.lilyVia.springbootinit.mapper.ChartCoreDataMapper;
import org.springframework.stereotype.Service;

/**
* @author 22825
* @description 针对表【chart_core_data(图表核心数据表)】的数据库操作Service实现
* @createDate 2024-04-05 14:28:12
*/
@Service
public class ChartCoreDataServiceImpl extends ServiceImpl<ChartCoreDataMapper, ChartCoreData>
    implements ChartCoreDataService{

}




