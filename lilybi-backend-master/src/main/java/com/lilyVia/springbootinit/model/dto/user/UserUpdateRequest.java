package com.lilyVia.springbootinit.model.dto.user;

import java.io.Serializable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户更新请求
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}