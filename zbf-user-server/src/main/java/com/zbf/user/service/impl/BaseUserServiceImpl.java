package com.zbf.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbf.user.entity.BaseUser;
import com.zbf.user.mapper.BaseUserMapper;
import com.zbf.user.service.IBaseUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-12
 */
@Service
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, BaseUser> implements IBaseUserService {

    @Override
    public BaseUser getBaseUserByLoginName(String loginName) {
        return baseMapper.getBaseUserByLoginName(loginName);
    }

    @Override
    public BaseUser getBaseUserByEmail(String email) {
        return baseMapper.getBaseUserByEmail(email);
    }

    @Override
    public BaseUser getBaseUserByTel(String phone) {
        return baseMapper.getBaseUserByTel(phone);
    }

    @Override
    public boolean upd(BaseUser baseUser) {
        return baseMapper.upd(baseUser);
    }

    @Override
    public IPage<BaseUser> getUserList(Page page, BaseUser vo) {
        return baseMapper.getUserList(page,vo);
    }

    @Override
    public BaseUser jihuo(Integer id) {
        return baseMapper.jihuo(id);
    }

    @Override
    public void updJhuo(BaseUser baseUser) {
        baseMapper.updJhuo(baseUser);
    }

    @Override
    public void addUser(BaseUser baseUser) {
        baseMapper.addUser(baseUser);
    }


}
