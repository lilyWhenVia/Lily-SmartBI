package com.lilyVia.springbootinit.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户视图（脱敏）
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;


    /**
     * 已使用ai使用次数
     */
    private int totalFrequency;


    /**
     * 剩余ai使用次数
     */
    private int remainFrequency;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;



    private static final long serialVersionUID = 1L;
}