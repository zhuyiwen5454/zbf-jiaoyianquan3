package com.zbf.auth.web;

import com.alibaba.fastjson.JSONObject;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.common.utils.JwtUtils;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/7  19:13
 * 描述: 登出处理
 */
@RestController
public class OauthLoginOutController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping("/loginout")
    public ResponseResult wuJinLogout(HttpServletRequest request){
        Map<String,Object> map=new HashMap<String,Object>();
        Enumeration<String> token = request.getHeaders("token");
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(token.hasMoreElements()){
            String s = token.nextElement();
            JSONObject jsonObject = JwtUtils.decodeJwtTocken(s);
            Long info = redisTemplate.opsForHash().delete("user-auth", jsonObject.getString("info"));
            if(info==1){
                //退出成功
                responseResult.setCode(AllStatusEnum.LOGINOUT_SUCCESS.getCode());
                responseResult.setMsg(AllStatusEnum.LOGINOUT_SUCCESS.getMsg());
            }else{
                //退出失败
                responseResult.setCode(AllStatusEnum.LOGINOUT_FAIRLE.getCode());
                responseResult.setMsg(AllStatusEnum.LOGINOUT_FAIRLE.getMsg());
            }
        }
        return responseResult;
    }

}
