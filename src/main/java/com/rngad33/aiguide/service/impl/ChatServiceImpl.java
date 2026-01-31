package com.rngad33.aiguide.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.rngad33.aiguide.mapper.ChatMapper;
import com.rngad33.aiguide.model.entity.Chat;
import com.rngad33.aiguide.service.ChatService;
import org.springframework.stereotype.Service;

/**
 * 对话记录服务实现
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

}