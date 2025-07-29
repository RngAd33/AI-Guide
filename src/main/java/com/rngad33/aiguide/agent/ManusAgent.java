package com.rngad33.aiguide.agent;

import com.rngad33.aiguide.constant.SystemPromptsConstant;
import com.rngad33.aiguide.utils.AiModelUtils.MyChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * Manus代理
 */
@Component
public class ManusAgent extends ToolCallAgent {

    public ManusAgent(ToolCallback[] allTools, MyChatModel chatModel) {
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