package com.zbf.auth.denglu;

import com.alibaba.fastjson.JSON;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
 * 日期: 2020/9/6  23:08
 * 描述: 登录失败处理器
 */
@Component
public class MyLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        System.out.println("登陆失败处理器");

        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setMsg(AllStatusEnum.LOGINOUT_FAIRLE.getMsg());
        responseResult.setCode(AllStatusEnum.LOGINOUT_FAIRLE.getCode());

        PrintWriter writer = httpServletResponse.getWriter();
        writer.print(JSON.toJSONString(responseResult));
        writer.flush();
        writer.close();
    }
}
