package com.rngad33.aiguide.controller;

import cn.hutool.core.lang.UUID;
import com.rngad33.aiguide.app.PsychologyApp;
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
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private PsychologyApp psychologyApp;

    /**
     * 小姐姐心理疏导
     *
     * @param prompt
     * @return
     */
    @PostMapping("/psy")
    public String doChat(@RequestBody String prompt) {
        String chatId = UUID.randomUUID().toString();
        return psychologyApp.doChat(prompt, chatId);
    }

}