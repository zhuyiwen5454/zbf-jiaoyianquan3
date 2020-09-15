package com.zbf.user.service;

import com.zbf.user.entity.BaseUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wts
 * @since 2020-09-12
 */
public interface IBaseUserService extends IService<BaseUser> {

    /**
     *
     * @param loginName
     * @return
     * 通过用户名查询
     */
    @Select("select * from base_user where loginName = #{loginName}")
    public BaseUser getBaseUserByLoginName(String loginName);

    /**
     * @param email
     * @return
     * 通过邮箱查询
     */
    @Select("select * from base_user where email = #{email}")
    public BaseUser getBaseUserByEmail(String email);

    /**
     * @param phone
     * @return
     * 通过手机号查询
     */
    @Select("select * from base_user where tel = #{tel}")
    public BaseUser getBaseUserByTel(String phone);

    /**
     * 通过id修改密码
     */
    @Update("UPDATE base_user SET passWord=#{passWord} WHERE id=#{id}")
    public boolean upd(BaseUser baseUser);

}
