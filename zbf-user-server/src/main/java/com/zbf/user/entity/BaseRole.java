package com.zbf.user.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseRole implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 这是角色表
     */
    private Long id;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色表
     */
    private String name;

    /**
     * 描述
     */
    private String miaoshu;


}
