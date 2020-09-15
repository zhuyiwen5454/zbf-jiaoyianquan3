package com.zbf.auth.denglu;

import com.alibaba.fastjson.JSON;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
 * 日期: 2020/9/6  23:18
 * 描述: 访问拒绝处理辅助类
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        ResponseResult responseResult = ResponseResult.getResponseResult();
        //没有权限的异常信息
        responseResult.setMsg(AllStatusEnum.NO_AUTH.getMsg());
        responseResult.setCode(AllStatusEnum.NO_AUTH.getCode());
        PrintWriter writer = httpServletResponse.getWriter();
        writer.print(JSON.toJSONString(responseResult));
        writer.flush();
        writer.close();
    }
}
