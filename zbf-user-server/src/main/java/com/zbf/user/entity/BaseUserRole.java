package com.zbf.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wts
 * @since 2020-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseUserRole implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    @TableField("roleId")
    private Long roleId;

    @TableField("userId")
    private Long userId;


}
