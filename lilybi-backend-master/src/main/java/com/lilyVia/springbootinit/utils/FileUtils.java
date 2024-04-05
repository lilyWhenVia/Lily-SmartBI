package com.lilyVia.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.exception.BusinessException;
import com.lilyVia.springbootinit.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lily via on 2024/3/14 23:59
 * 处理excel等数据文件转换为csv格式的工具类
 */
@Slf4j
public class FileUtils {

    // todo 数据正则获取
    public static String getFileString(MultipartFile multipartFile){
        if (multipartFile==null||ObjectUtils.isEmpty(multipartFile)){
            log.error("multipartFile is null or empty");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "multipartFile wrong");
        }
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /**
         * 拼接文件字符串
         */
        ThrowUtils.throwIf(CollUtil.isEmpty(list), ErrorCode.PARAMS_ERROR, "文件读取失败");
        StringBuffer data = new StringBuffer();
        Iterator<Map<Integer, String>> it = list.iterator();
        while (it.hasNext()){
            /**
             * 过滤空行
             */
            Map<Integer, String> row = it.next();
            if (CollUtil.isEmpty(row)) continue;
            for (String value : row.values()) {
                /**
                 * 过滤空单元格
                 */
                if (StringUtils.isEmpty(value)||StringUtils.isBlank(value)) continue;
                data.append(value);
                data.append(',');
            }
            data.append("\\n");
        }
        return data.toString();
    }


}
