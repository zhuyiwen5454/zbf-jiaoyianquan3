package com.zbf.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/7  22:48
 * 描述:
 */
@ComponentScan
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean("redisTemplateAuthentication")
    public RedisTemplate<String,Authentication> getRedisTemplateAuthentication(){
        RedisTemplate<String, Authentication> stringAuthenticationRedisTemplate = new RedisTemplate<>();
        stringAuthenticationRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringAuthenticationRedisTemplate;
    }

    @Bean("redisTemplateString")
    public RedisTemplate<String,String> getRedisTemplateString(){
        RedisTemplate<String, String> stringStringRedisTemplate = new RedisTemplate<>();
        stringStringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringStringRedisTemplate;
    }

    @Bean("redisTemplateObject")
    public RedisTemplate<String,Object> getRedisTemplateObject(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }


}
