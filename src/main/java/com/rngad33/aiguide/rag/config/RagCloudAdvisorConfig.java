package com.rngad33.aiguide.rag.config;

import com.rngad33.aiguide.constant.KnowledgeIndexConstant;
import com.rngad33.aiguide.rag.factory.VectorStoreFactory;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.context.annotation.Bean;

/**
 * RAG云知识库配置（用不起了555）
 */
@Deprecated
//@Configuration
public class RagCloudAdvisorConfig {

    @Resource
    private VectorStoreFactory vectorStoreFactory;

    /**
     * 初始化RAG云知识库 1
     *
     * @return
     */
    @Bean("loveAppRagCloudAdvisor")
    public Advisor loveAppRagCloudAdvisor() {
        return vectorStoreFactory.getVectorStore(KnowledgeIndexConstant.LOVE_INDEX);
    }

    /**
     * 初始化RAG云知识库 2
     *
     * @return
     */
    @Bean("psychologyAppRagCloudAdvisor")
    public Advisor psychologyAppRagCloudAdvisor() {
        return vectorStoreFactory.getVectorStore(KnowledgeIndexConstant.PSYCHOLOGY_INDEX);
    }

    /**
     * 初始化RAG云知识库 3
     *
     * @return
     */
    @Bean("gameAppRagCloudAdvisor")
    public Advisor gameAppRagCloudAdvisor() {
        return vectorStoreFactory.getVectorStore(KnowledgeIndexConstant.GAME_INDEX);
    }

}