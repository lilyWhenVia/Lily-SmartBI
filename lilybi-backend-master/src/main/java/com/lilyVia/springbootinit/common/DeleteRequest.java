package com.lilyVia.springbootinit.common;

import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * 删除请求
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * 图表id
     */
    @NotEmpty
    @NotBlank
    @Min(0L)
    private Long id;

    private static final long serialVersionUID = 1L;
}