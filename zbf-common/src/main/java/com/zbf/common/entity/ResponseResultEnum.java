package com.zbf.common.entity;

/**
 * 作者: LCG
 * 日期: 2020/5/31 21:43
 * 描述: ResponseResult的code值的意义
 */
public enum ResponseResultEnum {

    SECCESS("登录成功",0),FAILURE("失败",1),REGISTER("注册成功",2);

    private String message;

    private int code;

    ResponseResultEnum(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
