package com.zbf.auth.mapper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: zhuyiwen
 * 作者: zhuyiwen
 * 日期: 2020/9/10 18:58
 * 描述:
 */
@FeignClient(value = "getCode-server")
public interface GetCodeDao {

    @RequestMapping("sendCode")
    public boolean sendCode(@RequestParam("tel") String tel);
}
