package com.lilyVia.springbootinit.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Optional;

/**
 * SQL 工具
 *
 * @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
public class SqlUtils {

    private static final String[] orderField = {"name", "updateTime", "createTime", "genResult", "goal"};
    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(@NotBlank @NotEmpty String sortField) throws BusinessException {
        boolean containsAny = StringUtils.containsAny(sortField, "=", "(", ")", " ");

        if (containsAny){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return true;
    }

    public static boolean validSortOrder(@NotBlank @NotEmpty String sortField) throws BusinessException {
        boolean isField = Arrays.stream(orderField).anyMatch(s -> s.equals(sortField));
        if (!isField){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return true;
    }

}
