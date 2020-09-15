package com.zbf.auth.web;

import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/6  23:24
 * 描述: 登录处理类
 */
@RestController
public class OuathLoginController {

    @RequestMapping("/loginPage")
    public ResponseResult loginPage(){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(AllStatusEnum.NO_LOGIN.getCode());
        responseResult.setMsg(AllStatusEnum.NO_LOGIN.getMsg());
        return responseResult;
    }

}
