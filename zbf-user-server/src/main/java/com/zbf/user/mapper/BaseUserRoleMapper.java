package com.zbf.user.mapper;

import com.zbf.user.entity.BaseUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-16
 */
@Mapper
public interface BaseUserRoleMapper extends BaseMapper<BaseUserRole> {
    @Insert("INSERT INTO base_user_role(roleId,userId) VALUES(#{rid},#{userId})")
    void addUserRole(Long userId, Long rid);
}
