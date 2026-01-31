package com.rngad33.aiguide.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.util.Date;

/**
 * 对话记录模型
 */
@Data
@Table("chat")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chat {

    /**
     * id
     */
    private Long id;

    /**
     * 聊天室 id
     */
    private Long roomId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 用户提问
     */
    private String question;

    /**
     * AI 回复
     */
    private String answer;

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