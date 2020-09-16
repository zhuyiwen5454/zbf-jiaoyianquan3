package com.zbf.user.service;

import com.zbf.user.entity.BaseMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wts
 * @since 2020-09-16
 */
public interface IBaseMenuService extends IService<BaseMenu> {

    public List<BaseMenu> getMenuList();
}
