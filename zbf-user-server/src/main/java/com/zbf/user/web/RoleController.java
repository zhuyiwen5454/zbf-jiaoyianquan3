package com.zbf.user.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zbf.user.entity.BaseRole;

import com.zbf.user.service.IBaseRoleService;
import com.zbf.user.service.IBaseUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {
    //角色管理控制层
    @Autowired
    private IBaseRoleService iBaseRoleService;

    @Autowired
    private IBaseUserRoleService iBaseUserRoleService;

    /**
     * getRoleList方法 获取模糊查询的角色信息
     */
    @RequestMapping("/getRoleList")
    public List<BaseRole> bindRoleForUser(@RequestParam String name){
        QueryWrapper<BaseRole> wrapper = new QueryWrapper<>();
        wrapper.like("name",name);
        List<BaseRole> list = iBaseRoleService.list(wrapper);
        return list;
    }
    /**
     * 绑定角色操作
     */
    @RequestMapping("/bindRoleForUser")
    public boolean addUserRole(@RequestParam Long userId,@RequestParam Long[] roleId){
        System.out.println("userid:"+userId+"-------roleid:"+roleId);
        //添加中间表
        for(Long rid:roleId){
            //添加中间表
            iBaseUserRoleService.addUserRole(userId,rid);
        }
        return true;
    }
}
