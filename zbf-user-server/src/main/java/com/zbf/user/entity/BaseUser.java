package com.zbf.user.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.zbf.user.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhuyiwen
 * @since 2020-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseUser implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户表
     */
    private Long id;

    private Integer version;

    @TableField("userName")
    private String userName;

    @TableField("loginName")
    private String loginName;

    @TableField("img")
    private String img;

    @TableField("passWord")
    private String passWord;

    private String tel;

    @TableField("buMen")
    private String buMen;

    private String salt;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("updateTime")
    private LocalDateTime updateTime;

    @TableField("email")
    private String email;

    @TableField("gender")
    private Gender gender;

    @TableField("status")
    private Integer status;

    @TableField(exist = false)
    private String checkPass;

    @TableField(exist = false)
    private String rname;
    @TableField(exist = false)
    private String rid;

}
