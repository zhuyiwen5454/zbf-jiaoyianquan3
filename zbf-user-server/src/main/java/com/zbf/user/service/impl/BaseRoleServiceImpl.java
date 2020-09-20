package com.zbf.user.service.impl;

import com.zbf.user.entity.BaseRole;
import com.zbf.user.mapper.BaseRoleMapper;
import com.zbf.user.service.IBaseRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-16
 */
@Service
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, BaseRole> implements IBaseRoleService {

    @Override
    public List<BaseRole> bindRoleForUser(String name) {
        return baseMapper.bindRoleForUser(name);
    }
}
