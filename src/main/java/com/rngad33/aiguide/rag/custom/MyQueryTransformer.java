package com.rngad33.aiguide.rag.custom;

import com.rngad33.aiguide.utils.AiModelUtils.MyChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 查询翻译器（语言转换）
 * ——！使用此模块会提高成本，建议使入翻译平台API！——
 */
@Component
public class MyQueryTransformer {

    private final QueryTransformer queryTransformer;

    public MyQueryTransformer(MyChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        // 创建查询翻译器
        queryTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(builder)
                .targetLanguage("chinese")  // 设置目标语言为中文
                .build();
    }

    /**
     * 执行查询翻译
     *
     * @param prompt
     * @return
     */
    public String doTransform(String prompt) {
        // 执行查询翻译
        Query transformedQuery = queryTransformer.transform(new Query(prompt));
        // 返回翻译后的查询文本
        return transformedQuery.text();
    }

}