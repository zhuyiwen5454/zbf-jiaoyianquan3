package com.zbf.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Gender {
    WOMAN(0,"女"),MAN(1,"男");

    @EnumValue
    private Integer value;
    private String lable;

    Gender(Integer value,String lable){
        this.value=value;
        this.lable=lable;
    }
}

