package com.rngad33.aiguide.rag.factory;

import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 工厂模式
 */
public class RagCustomAdvisorFactory {

    /**
     * 创建自定义RAG检索增强拦截器
     *
     * @param vectorStore 向量存储
     * @param scene 场景
     * @return 自定义RAG检索增强拦截器
     */
    public static Advisor createRagCustomAdvisor(VectorStore vectorStore, String scene) {
        // 创建过滤表达式（过滤条件）
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("scene", scene)
                .build();
        // 创建文档检索器
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .filterExpression(expression)   // 过滤条件
                .similarityThreshold(0.5)   // 相似度阈值
                .topK(3)   // 文档数量
                .build();
        // 创建上下文查询增强器
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(ContextualQueryAugmenterFactory.createInstance())
                .build();
    }

}