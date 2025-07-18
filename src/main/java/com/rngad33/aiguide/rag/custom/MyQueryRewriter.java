package com.rngad33.aiguide.rag.custom;

import com.rngad33.aiguide.utils.AiModelUtils.MyChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 查询重写器（提高回答质量）
 */
@Component
public class MyQueryRewriter {

    private final QueryTransformer queryTransformer;

    public MyQueryRewriter(MyChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        // 创建查询重写器
        queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    /**
     * 执行查询重写
     *
     * @param prompt
     * @return
     */
    public String doRewrite(String prompt) {
        // 执行查询重写
        Query rewritedQuery = queryTransformer.transform(new Query(prompt));
        // 返回重写后的查询文本
        return rewritedQuery.text();
    }

}