package com.zbf.user.mapper;

import com.zbf.user.entity.BaseRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-16
 */
@Mapper
public interface BaseRoleMapper extends BaseMapper<BaseRole> {
    @Select("SELECT * FROM base_role br WHERE br.`name` like concat('%',#{name},'%')")
    List<BaseRole> bindRoleForUser(String name);
}
