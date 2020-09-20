package com.zbf.user.service;

import com.zbf.user.entity.BaseRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-16
 */
public interface IBaseRoleService extends IService<BaseRole> {

    List<BaseRole> bindRoleForUser(String name);
}
