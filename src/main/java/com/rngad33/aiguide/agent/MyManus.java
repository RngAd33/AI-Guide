package com.rngad33.aiguide.agent;

import com.rngad33.aiguide.constant.SystemPromptsConstant;
import com.rngad33.aiguide.utils.AiModelUtils.MyChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * Manus AI智能体
 */
@Component
public class MyManus extends ToolCallAgent {

    public MyManus(ToolCallback[] allTools, MyChatModel chatModel) {
        super(allTools);
        this.setName("ManusAgent");
        this.setSystemPrompt(SystemPromptsConstant.MANUS_SYSTEM_PROMPT);
        this.setNextStepPrompt(SystemPromptsConstant.MANUS_NEXT_STEP_PROMPT);
        this.setMaxSteps(20);
        // 初始化客户端
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultTools(allTools)
                .build();
        this.setChatClient(chatClient);
    }

}