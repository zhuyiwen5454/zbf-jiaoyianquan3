package com.zbf.user.api;

import com.zbf.user.mapper.BaseMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName tongdaowei
 * @Description TODO
 * @Author Administrator
 * @Date 2020/9/16 0016 下午 6:30
 * @Version 1.0
 **/
@Service
public class MenuService {
    @Autowired
    private BaseMenuMapper menuMapper;


    /**
      *@Author tongdaowei
      *@Description //TODO
      *@Date 2020/9/16 0016 下午 6:31
      *@Param [userHash]
      *@return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
      *@miaoshu  获取某个用户的权限列表
    **/
    public List<Map<String,Object>> getlistMenu(HashMap<String, Object> userHash) {
        //获取一级菜单
        List<Map<String, Object>> list = menuMapper.getByLoginNameMenu(userHash.get("loginName").toString());

        this.getMenuList(list,userHash.get("loginName").toString());
        return list;
    }


    /**
      *@Author tongdaowei
      *@Description //TODO
      *@Date 2020/9/16 0016 下午 6:31
      *@Param [list, loginName]
      *@return void
      *@miaoshu 根据一级菜单获取下级菜单
    **/
    private void getMenuList(List<Map<String, Object>> list, String loginName) {

        for (Map<String, Object> menu:list) {
            //获取下级菜单
            HashMap<Object, Object> paramHash = new HashMap<>();
            paramHash.put("loginName",loginName);
            paramHash.put("leval",Integer.valueOf(menu.get("leval").toString())+1);
            paramHash.put("parentCode",menu.get("code"));
            List<Map<String, Object>> nextMenu = menuMapper.getNextMenu(paramHash);
            if(nextMenu.size()>0){
                menu.put("listMenu",nextMenu);
                this.getMenuList(nextMenu,loginName);
            }else{
                nextMenu =  new ArrayList<>();
                menu.put("listMenu",nextMenu);
                break;
            }
        }
    }
}
