package com.rngad33.aiguide.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.mask.Masks;
import lombok.Data;

import java.util.Date;

/**
 * 用户
 */
@Data
@Table("user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    /**
     * id
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 电话
     */
    @ColumnMask(Masks.FIXED_PHONE)
    private String phone;

    /**
     * 密码
     */
    @ColumnMask(Masks.PASSWORD)
    private String userPassword;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 用户身份：0-普通用户，1-管理员
     */
    private Integer role;

    /**
     * 用户状态：0-正常，1-封禁
     */
    private Integer status;

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

}