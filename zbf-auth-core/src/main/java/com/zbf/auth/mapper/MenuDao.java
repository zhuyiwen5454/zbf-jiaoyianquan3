package com.zbf.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 作者: LCG
 * 日期: 2020/8/27 19:52
 * 描述:
 */
@Mapper
public interface MenuDao {

    /**
     * 获取所有的权限信息，以及权限对应的角色
     * @return
     */
    @Select("select CONCAT(br.role_code,\":\",bm.url ) urlRoleCode from base_menu bm inner join base_role_menu brm on brm.menu_id=bm.id INNER JOIN base_role br on br.id=brm.role_id where url!=null or url!=''")
    public List<Map<String,String>>  getAllMenus();

    /**
     * 根据URl 获取对应的角色列表
     * @param url
     * @return
     */
    @Select("select br.role_code roleCode from base_menu bm inner join base_role_menu brm on bm.id=brm.menu_id inner join base_role br on br.id=brm.role_id where bm.url=#{url} GROUP BY role_code")
    public List<String> getRoles(String url);

}
