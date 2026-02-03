package com.rngad33.aiguide.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 创建聊天室请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomCreateRequest {

    /**
     * 聊天室 id
     */
    private String chatRoomId;

    /**
     * 归属应用号码
     */
    private Integer appCode;

    /**
     * 创建用户 id
     */
    private Long userId;

}