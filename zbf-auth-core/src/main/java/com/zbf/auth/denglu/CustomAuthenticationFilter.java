package com.zbf.auth.denglu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/7  11:29
 * 描述: 将SpringSecurity的表单登录格式改为JSON格式的登录
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //attempt Authentication when Content-Type is json
        if("/forLogin".equals(getCurrentAccessUrl(request))){
            //use jackson to deserialize json
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()){
                Map<String,String> authenticationBean = mapper.readValue(is,Map.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        authenticationBean.get("userName"), authenticationBean.get("password"));
            }catch (IOException e) {
                //e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken("", "");
            }finally {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }else {
            return super.attemptAuthentication(request, response);
        }
    }

    /**
     * 作者: LCG
     * 日期: 2020/9/9  20:49
     * 参数：
     * 返回值：
     * 描述: 获取请求的路径
     */
    public String getCurrentAccessUrl(HttpServletRequest request){
        String requestURI = request.getRequestURI();
        return requestURI;
    }
}