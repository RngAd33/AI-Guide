package com.rngad33.aiguide.config;

import com.knuddels.jtokkit.api.EncodingType;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 嵌入式配置
 */
@Configuration
public class EmbeddingConfig {

    /**
     * 初始化自定义 token 计数批处理策略
     *
     * @return
     */
    @Bean
    public BatchingStrategy customTokenCountBatchingStrategy() {
        return new TokenCountBatchingStrategy(
                EncodingType.CL100K_BASE,
                8000,
                0.1
        );
    }

}