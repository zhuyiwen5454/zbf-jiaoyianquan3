package com.zbf.common.entity;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/10  15:55
 * 描述:
 */
public enum AllRedisKeyEnum {
    //用户信息的key
    USER_INFO_KEY("user-info","用户信息key"),
    //用户权限的key
    USER_AUTH_KEY("user-auth","用户权限的key"),
    //系统所有权限的key
    ALL_MENU_KEY("menuRole","系统所有权限的key"),
    //激活码的key
    ACTIVIVE_KEY("active","激活码的key"),
    //验证码的Key
    YAN_ZHENG_KEY("yanzheng","验证码的Key"),
    //用户的权限菜单信息
    USER_MENU_KEY("user-menu","用户的权限菜单");

    /*Redis的Key*/
    private String key;

    /*Redis的key的描述*/
    private String msg;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    AllRedisKeyEnum(String key, String msg) {
        this.key = key;
        this.msg = msg;
    }
}
