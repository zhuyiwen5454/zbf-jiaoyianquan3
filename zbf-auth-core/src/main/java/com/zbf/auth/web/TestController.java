package com.zbf.auth.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: WTS
 * 作者: WTS
 * 日期: 2020/9/9  20:24
 * 描述:
 */
@RestController
public class TestController {


    @RequestMapping("menu")
    public String test(){
        return "OK==测试";
    }

}
