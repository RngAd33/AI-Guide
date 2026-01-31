package com.rngad33.aiguide.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.util.Date;

/**
 * 对话记录
 */
@Data
@Table("chat")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chat {

    /**
     * id
     */
    @Id(keyType = KeyType.Generator)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 对话内容（JSON）
     */
    private String content;

    /**
     * 创建用户id
     */
    private Long userId;

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