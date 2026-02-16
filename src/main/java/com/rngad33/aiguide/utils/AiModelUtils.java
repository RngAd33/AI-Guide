package com.rngad33.aiguide.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话模型工具类
 * 通过配置 application.yml 调整要调用的大模型引擎（阿里灵积 / ollama）
 */
@Component
@Slf4j
public class AiModelUtils {

    /**
     * 自定义对话模型
     */
    @Component
    public static class MyChatModel implements ChatModel {

        @Resource
        private ApplicationContext context;

        @Value("${ai.model-type}")
        private String activeModelType;

        private ChatModel delegate() {
            return switch (activeModelType.toLowerCase()) {
                case "dashscope" -> context.getBean("dashscopeChatModel", ChatModel.class);
                case "ollama" -> context.getBean("ollamaChatModel", ChatModel.class);
                default -> throw new IllegalArgumentException("未知模型类型：" + activeModelType);
            };
        }

        @Override
        public ChatResponse call(Prompt prompt) {
            return delegate().call(prompt);
        }

        @Override
        public Flux<ChatResponse> stream(Prompt prompt) {
            return delegate().stream(prompt);
        }
    }

    /**
     * 自定义嵌入式模型
     */
    @Component
    public static class MyEmbeddingModel implements EmbeddingModel {

        @Resource
        private ApplicationContext context;

        @Value("${ai.model-type}")
        private String activeModelType;

        private EmbeddingModel delegate() {
            return switch (activeModelType.toLowerCase()) {
                case "dashscope" -> context.getBean("dashscopeEmbeddingModel", EmbeddingModel.class);
                case "ollama" -> context.getBean("ollamaEmbeddingModel", EmbeddingModel.class);
                default -> throw new IllegalArgumentException("未知模型类型：" + activeModelType);
            };
        }

        @Override
        public float[] embed(String text) {
            return delegate().embed(text);
        }

        @Override
        public List<float[]> embed(List<String> texts) {
            // 分批处理，每批最多25个文本
            int batchSize = 25;
            List<float[]> result = new ArrayList<>();
            for (int i = 0; i < texts.size(); i += batchSize) {
                List<String> batch = texts.subList(i, Math.min(i + batchSize, texts.size()));
                log.debug("Processing batch with size: {}", batch.size());
                try {
                    // 调用委托模型处理当前批次
                    result.addAll(delegate().embed(batch));
                } catch (IllegalArgumentException e) {
                    // 记录异常日志
                    log.error("嵌入模型调用失败，输入文本数量超出限制: {}", e.getMessage());
                    throw new RuntimeException("Failed to process embedding batch", e); // 抛出异常以便上层处理
                }
            }
            return result;
        }

        @Override
        public float[] embed(Document document) {
            return delegate().embed(document);
        }

        @Override
        public EmbeddingResponse call(EmbeddingRequest request) {
            return delegate().call(request);
        }
    }
}