package com.rngad33.aiguide.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.rngad33.aiguide.model.enums.agent.AgentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具调用基础代理类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    // 可用工具
    private final ToolCallback[] availableTools;

    // 响应结果
    private ChatResponse toolCallChatResponse;

    // 工具管理器
    private final ToolCallingManager toolCallingManager;

    // 禁用Spring AI内部工具调用机制，改为自行维护
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()   // 此处导致只能使用阿里大模型
                .withProxyToolCalls(true)
                .build();
    }

    /**
     * 处理当前状态并决定下一步行动
     *
     * @return 是否需要执行行动，true表示需要执行，false表示不需要执行
     */
    @Override
    public boolean think() {
        // 校验、拼接提示词
        if (StrUtil.isNotBlank(getNextStepPrompt())) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessages().add(userMessage);
        }
        // 获取工具调用结果
        List<Message> messageList = getMessages();
        Prompt prompt = new Prompt(messageList, chatOptions);
        // 解析工具调用结果，获取要调用的工具
        try {
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();
            this.toolCallChatResponse = chatResponse;
            // 助手信息
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 工具列表
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();
            // 输出提示信息
            String result = assistantMessage.getText();
            log.info(getName() + "的调用结果：" + result);
            log.info(getName() + " 选择了 " + toolCallList.size() + " 个工具来使用");
            String toolCallInfo = toolCallList.stream()
                    .map(toolCall -> String.format("工具名称：%s，参数：%s",
                                toolCall.name(),
                                toolCall.arguments())
                    )
                    .collect(Collectors.joining("\n"));
            log.info(toolCallInfo);
            // 如果无需调用工具，返回false
            if (toolCallList.isEmpty()) {
                // 只有不调用工具时，才需要手动记录助手消息
                getMessages().add(assistantMessage);
                return false;
            } else {
                // 调用工具时无需记录助手消息，因为工具调用时会自动记录
                return true;
            }
        } catch (Exception e) {
            // 异常处理
            log.error(getName() + "的思考过程遇到了问题: " + e.getMessage());
            getMessages().add(new AssistantMessage("处理失败：" + e.getMessage()));
            return false;
        }
    }

    /**
     * 执行决定的行动
     *
     * @return 行动执行结果
     */
    @Override
    public String act() {
        // 验证是否需要行动
        if (!toolCallChatResponse.hasToolCalls()) {
            return "无需调用工具ㄟ( ▔, ▔ )ㄏ-";
        }
        Prompt prompt = new Prompt(getMessages(), this.chatOptions);
        // 调用工具
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);
        // 记录消息上下文
        setMessages(toolExecutionResult.conversationHistory());
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage)CollUtil.getLast(toolExecutionResult.conversationHistory());
        String results = toolResponseMessage.getResponses().stream()
                .map(response -> "工具" +  response.name() + "返回的结果：" + response.responseData())
                .collect(Collectors.joining("\n"));
        // 判断是否调用了任务终止工具
        if (toolResponseMessage.getResponses().stream()
                .anyMatch(response -> response.name().equals("doTerminate"))) {
            // 任务结束，更改代理状态
            setStatus(AgentStatus.FINISHED);
        }
        log.info(results);
        return results;
    }

}