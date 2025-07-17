package com.rngad33.aiguide.controller;

import com.rngad33.aiguide.utils.AiModelUtils;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI会话接口
 */
@RestController
@RequestMapping("/")
public class ChatController {

    @Resource
    private AiModelUtils.ChatModelUtil chatModel;

    @PostMapping("/chat")
    public String doChat(@RequestBody String prompt) {
        return chatModel.call(new Prompt(prompt)).getResult().getOutput().getText();
    }

}