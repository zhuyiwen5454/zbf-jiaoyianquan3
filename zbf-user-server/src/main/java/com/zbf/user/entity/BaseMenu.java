package com.zbf.user.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class BaseMenu implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 菜单表
     */
    private Long id;

    private Integer version;

    private Long code;

    @TableField("menuName")
    private String menuName;

    @TableField("imagePath")
    private String imagePath;

    private String url;

    @TableField("parentCode")
    private Long parentCode;

    private Integer leval;

    @TableField("createTime")
    private Date createTime;


    @TableField(exist = false)
    private List<BaseUser> menuList;

}
