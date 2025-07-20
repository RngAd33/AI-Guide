package com.rngad33.aiguide.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.mask.Masks;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户模型
 *
 * @TableName user
 */
@Data
@Table(value = "user")
public class User implements Serializable {

    /**
     * 用户 id
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 身份？ 0-普通用户，1-管理员
     */
    private Integer role;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    @ColumnMask(Masks.PASSWORD)
    private String userPassword;

    /**
     * 电话
     */
    @ColumnMask(Masks.FIXED_PHONE)
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态：0-正常，1-封禁
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除？ 0-未删，1-已删
     */
    @Column(isLogicDelete = true)
    private Integer isDelete;

    @Column
    private static final long serialVersionUID = 3191241716373120793L;

}