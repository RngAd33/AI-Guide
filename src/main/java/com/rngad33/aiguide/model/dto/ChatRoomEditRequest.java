package com.rngad33.aiguide.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rngad33.aiguide.model.entity.Chat;
import lombok.Data;

import java.util.List;

/**
 * 聊天室编辑请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomEditRequest {

    /**
     * 聊天室 id
     */
    private String chatRoomId;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建用户 id
     */
    private Long userId;

}