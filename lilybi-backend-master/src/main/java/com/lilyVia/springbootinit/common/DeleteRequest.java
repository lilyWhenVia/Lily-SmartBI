package com.lilyVia.springbootinit.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 删除请求
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}