package com.zbf.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbf.user.entity.BaseUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-12
 */
@Mapper
public interface BaseUserMapper extends BaseMapper<BaseUser> {



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



    /**
     * 用户列表查询方法
     * @return
     */
    IPage<BaseUser> getUserList(Page page, BaseUser vo);

    /**
     * 激活
     * @param id
     * @return
     */
    @Select("select * from base_user bu WHERE bu.id=#{id}")
    BaseUser jihuo(Integer id);

    @Update("UPDATE base_user SET status=#{status} WHERE id=#{id}")
    void updJhuo(BaseUser baseUser);
}
