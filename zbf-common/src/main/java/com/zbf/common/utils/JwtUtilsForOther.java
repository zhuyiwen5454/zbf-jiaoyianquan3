package com.zbf.common.utils;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/10  15:31
 * 描述: 这个用来生成除去登录生成Token的其他的用途
 */
public class JwtUtilsForOther {

    private static String seginKey="dcbd8398cbda472f856fdddd274f3b7f";

    /**
     * 作者: LCG
     * 日期: 2020/9/10  15:33
     * 参数：info 表示需要加密的信息,timeout 超时时间的毫秒值
     * 返回值：
     * 描述: 使用JWT生成加密信息
     */
    public static String generateToken(String info,long timeout) {
        Map<String, Object> map = new HashMap<String,Object>();
        //用户信息
        map.put("info", info);
        //生成加密信息
        String compact = Jwts.builder()
                //payload 设置信息
                .setClaims(map)
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                //加密算法名称                    //签名的键
                .signWith(SignatureAlgorithm.HS512, seginKey).compact();

        return compact;

    }


    /**
     *
     * @param jwtString 待解密的jwt加密串（也就是生成的加密串）
     * @return
     */
    public static JSONObject decodeJwtTocken(String jwtString){
        //根据签名的key解密token信息
        Claims claims = Jwts.parser().setSigningKey(seginKey).parseClaimsJws(jwtString).getBody();
        //将用户信息转为JSONObject返回
        JSONObject info = new JSONObject();
        info.put("info",claims.get ( "info" ).toString ());
        return info;
    }



    /**
     * 作者: LCG
     * 日期: 2020/9/10  15:33
     * 参数：info 表示需要加密的信息,timeout 超时时间的毫秒值,seginKey 自己定义签名信息
     * 返回值：
     * 描述: 使用JWT生成加密信息
     */
    public static String generateJwtSelfDefinde(String info,long timeout,String seginKey) {
        Map<String, Object> map = new HashMap<String,Object>();
        //用户信息
        map.put("info", info);
        //生成加密信息
        String compact = Jwts.builder()
                //payload 设置信息
                .setClaims(map)
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                //加密算法名称                    //签名的键
                .signWith(SignatureAlgorithm.HS512, seginKey).compact();

        return compact;

    }


    /**
     *
     * @param jwtString 待解密的jwt加密串（也就是生成的加密串）,seginKey 自定义的签名信息
     * @return
     */
    public static JSONObject decodeJwtSelfDefinde(String jwtString,String seginKey){
        //根据签名的key解密token信息
        Claims claims = Jwts.parser().setSigningKey(seginKey).parseClaimsJws(jwtString).getBody();
        //将用户信息转为JSONObject返回
        JSONObject info = new JSONObject();
        info.put("info",claims.get ( "info" ).toString ());
        return info;
    }
}
