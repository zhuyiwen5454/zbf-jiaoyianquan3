package com.zbf.user.web;

import com.zbf.user.api.MenuService;
import com.zbf.user.entity.BaseMenu;
import com.zbf.user.entity.BaseUser;
import com.zbf.user.service.IBaseMenuService;
import com.zbf.user.service.IBaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: WTS
 * 作者: WTS
 * 日期: 2020/9/16 18:36
 * 描述:
 */
@RestController
public class MenuController {

    @Autowired
    private IBaseMenuService iBaseMenuService;

    @RequestMapping("list")
    public List<BaseMenu> getMenuList(){
        return iBaseMenuService.getMenuList();
    }
}
