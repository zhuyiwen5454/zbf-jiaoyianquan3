package com.zbf.auth.denglu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.common.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/7  11:09
 * 描述: 登录状态的过滤器，用这个过滤器保证认证中心服务在分布式的情况下可以保持登录的状态
 */
@Component
@Order(1)
public class MyAuthentactionAccessTokenFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserServiceDetail myUserServiceDetail;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取header中的验证信息
        Enumeration<String> atoken = request.getHeaders("token");
        String atokenValue=null;

        if(atoken.hasMoreElements()){
            atokenValue=atoken.nextElement();
        }

       if(getCurrentAccessUrl(request).equals("/forLogin")){
           filterChain.doFilter(request,response);
        }

        if (atokenValue != null&&!atokenValue.equals("null")&&!atokenValue.equals("undefined")) {

            JSONObject jsonObject = JwtUtils.decodeJwtTocken(atokenValue);
            String username =jsonObject.get("info").toString();

            //从token中获取用户信息，jwtUtils自定义的token加解密方式
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //根据用户名获取用户对象
                UserDetails userDetails = myUserServiceDetail.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //设置为已登录
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }

        filterChain.doFilter(request,response);
    }


    public String getCurrentAccessUrl(HttpServletRequest request){
        String requestURI = request.getRequestURI();
        return requestURI;
    }
}