package com.zbf.user.service.impl;

import com.zbf.user.entity.BaseMenu;
import com.zbf.user.mapper.BaseMenuMapper;
import com.zbf.user.service.IBaseMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-16
 */
@Service
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenu> implements IBaseMenuService {

    @Override
    public List<BaseMenu> getMenuList(String loginName) {
        return baseMapper.getMenuList(loginName);
    }

    @Override
    public List<BaseMenu> menuAll() {
        return baseMapper.menuAll();
    }
}
