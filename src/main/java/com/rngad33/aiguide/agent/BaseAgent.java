package com.rngad33.aiguide.agent;

import cn.hutool.core.util.StrUtil;
import com.rngad33.aiguide.model.enums.agent.AgentStatus;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.utils.ThrowUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心代理抽象类
 */
@Data
@Slf4j
public abstract class BaseAgent {

    // 核心属性
    private String name;
    private String description;

    // 提示词
    private String systemPrompt;
    private String nextStepPrompt;

    // 代理状态
    private AgentStatus status = AgentStatus.IDLE;

    // 执行步数控制
    private int maxSteps = 20;
    private int currentStep = 0;

    // LLM大模型
    private ChatClient chatClient;

    // 自主维护对话上下文记忆
    private List<Message> messages = new ArrayList<>();

    /**
     * 执行代理
     *
     * @param userPrompt 用户提示词
     * @return
     */
    public String run(String userPrompt) {
        // 基础校验
        ThrowUtils.throwIf(this.status != AgentStatus.IDLE, ErrorCodeEnum.PARAMS_ERROR, "代理已经开启！");
        ThrowUtils.throwIf(StrUtil.isBlank(userPrompt), ErrorCodeEnum.PARAMS_ERROR ,"空提示词不可执行！");
        // 执行
        this.status = AgentStatus.RUNNING;
        // - 记录消息上下文
        messages.add(new UserMessage(userPrompt));
        // - 执行循环
        List<String> results = new ArrayList<>();
        try {
            for (int i = 0; i < maxSteps && status != AgentStatus.FINISHED; i++) {
                int step = i + 1;
                currentStep = step;
                // - 单步执行
                String stepResult = step();
                String result = "Step " + step + ": " + stepResult;
                results.add(result);
            }
            // 检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                status = AgentStatus.FINISHED;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            status = AgentStatus.ERROR;
            log.error("Error executing agent: ", e);
            return e.getMessage();
        } finally {
            // 清理资源
            this.cleanUp();
        }
    }

    /**
     * 定义单个步骤
     *
     * @return
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanUp() {
        // 清空消息上下文
        if (messages != null) {
            messages.clear();
            messages = null;   // 显式置空引用
        }
        // 重置步数和状态
        this.currentStep = 0;
        this.status = AgentStatus.IDLE;
    }


}