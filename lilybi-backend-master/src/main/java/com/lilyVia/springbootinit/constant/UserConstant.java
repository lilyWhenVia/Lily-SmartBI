package com.lilyVia.springbootinit.constant;

/**
 * 用户常量
 *
* @author lily via <a href="https://github.com/lilyWhenVia">lily</a>
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    String USER_AI_Frequency = "USER_AI_Frequency";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion
}
