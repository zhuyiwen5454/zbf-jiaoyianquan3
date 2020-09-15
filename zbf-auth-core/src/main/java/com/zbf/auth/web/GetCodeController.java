package com.zbf.auth.web;

import com.zbf.auth.mapper.GetCodeDao;
import com.zbf.common.utils.MailQQUtils;
import com.zbf.common.utils.RanDomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

/**
 * @author: WTS
 * 作者: WTS
 * 日期: 2020/9/10 18:20
 * 描述:获取验证码
 */
@RestController
public class GetCodeController {

    @Autowired
    private GetCodeDao getCodeDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final Pattern PATTERN_PHONE = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    // 手机验证正则比对
    public boolean isPhone(String phone){
        return PATTERN_PHONE.matcher(phone).matches();
    }

    /**
     * 获取验证码
     * @param tel
     * @return
     */
    @RequestMapping("getCode")
    public void getCode(@RequestParam("tel") String tel){
        /**
         * 判断手机号发送短信,调用发送短信的sendCode方法
         */
        if(isPhone(tel)){
            System.out.println(tel);
            boolean b = getCodeDao.sendCode(tel);
        }else{
            /**
             * 如果不是手机号就向邮箱发送验证码,调用发送邮箱MailQQUtils的sendCode方法发送验证码
             */
            String fourRandom = RanDomUtils.getFourRandom();
            redisTemplate.opsForValue().set("code",fourRandom);
            MailQQUtils.sendCode(tel,fourRandom,"http://localhost:9090");
        }
    }
}
