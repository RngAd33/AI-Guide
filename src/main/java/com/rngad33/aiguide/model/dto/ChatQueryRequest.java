package com.rngad33.aiguide.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rngad33.aiguide.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 聊天记录查询请求
 */
@Deprecated
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatQueryRequest extends PageRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 聊天室 id
     */
    private String chatRoomId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

}