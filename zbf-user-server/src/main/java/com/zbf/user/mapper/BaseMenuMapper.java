package com.zbf.user.mapper;

import com.zbf.user.entity.BaseMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wts
 * @since 2020-09-16
 */
@Mapper
public interface BaseMenuMapper extends BaseMapper<BaseMenu> {

    public List<BaseMenu> getMenuList();
}
