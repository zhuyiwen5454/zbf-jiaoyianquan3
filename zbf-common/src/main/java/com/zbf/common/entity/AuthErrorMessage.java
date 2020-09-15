package com.zbf.common.entity;


/**
 * 作者: LCG
 * 日期: 2019/12/19 15:38
 * 描述: 定义一些访问失败的错误信息
 */
public enum AuthErrorMessage {
    //没有权限
    NO_AUTH("NOAUTH",1000)
    //没有登录
    ,NO_LOGIN("NOLOGIN",1001)
    //登陆过期
    ,HAS_EXPIRE("EXPIRE",1002);

    private String message;

    private int code;

    AuthErrorMessage(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
