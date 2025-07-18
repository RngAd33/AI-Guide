package com.rngad33.aiguide.rag.custom;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 工厂模式（创建自定义RAG检索增强拦截器）
 */
public class AppRagCustomAdvisorFactory {

    /**
     * 创建拦截器
     *
     * @return
     */
    public Advisor createRagCustomAdvisor(VectorStore vectorStore, String status) {
        // 创建过滤表达式
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status);

        // 创建文档检索器
        VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .filterExpression(filterExpression);

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever()
                .queryAugmenter()
                .build();
    }

}