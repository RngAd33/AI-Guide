package com.rngad33.aiguide.rag.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import com.rngad33.aiguide.constant.KnowledgeIndexConstant;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RAG云知识库配置
 */
@Configuration
public class RagCloudAdvisorConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;

    /**
     * 初始化RAG云知识库 1
     *
     * @return
     */
    @Bean("loveAppRagCloudAdvisor")
    public Advisor loveAppRagCloudAdvisor() {
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KnowledgeIndexConstant.LOVE_INDEX)
                        .build());
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();
    }

    /**
     * 初始化RAG云知识库 2
     *
     * @return
     */
    @Bean("psychologyAppRagCloudAdvisor")
    public Advisor psychologyAppRagCloudAdvisor() {
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KnowledgeIndexConstant.PSYCHOLOGY_INDEX)
                        .build());
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();
    }

    /**
     * 初始化RAG云知识库 3
     *
     * @return
     */
    @Bean("tetosoupAppRagCloudAdvisor")
    public Advisor tetosoupAppRagCloudAdvisor() {
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KnowledgeIndexConstant.TETO_INDEX)
                        .build());
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();
    }

}