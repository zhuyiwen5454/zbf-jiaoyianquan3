package com.zbf.auth.denglu;

import com.alibaba.fastjson.JSON;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/6  23:05
 * 描述: 登录成功处理器
 */

@Component
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate<String,String>   redisTemplateString;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setMsg(AllStatusEnum.LOGIN_SUCCESS.getMsg());
        responseResult.setCode(AllStatusEnum.LOGIN_SUCCESS.getCode());

        System.out.println("登陆成功处理器");

        responseResult.setUserInfo(authentication.getPrincipal().toString());
        //将用户名信息生成Token
        String token = JwtUtils.generateToken(authentication.getPrincipal().toString());
        httpServletResponse.setHeader("token",token);
        PrintWriter writer = httpServletResponse.getWriter();
        writer.print(JSON.toJSONString(responseResult));
        writer.flush();
        writer.close();

    }
}
