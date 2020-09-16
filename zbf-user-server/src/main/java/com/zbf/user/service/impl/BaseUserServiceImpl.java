package com.zbf.user.service.impl;

import com.zbf.user.entity.BaseUser;
import com.zbf.user.mapper.BaseUserMapper;
import com.zbf.user.service.IBaseUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wts
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

}
