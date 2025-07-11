package com.rngad33.aiguide.config;

import com.rngad33.aiguide.rag.PsychologyAppDocumentLoader;
import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 * PG向量数据库配置
 */
@Configuration
public class PGVectorStoreConfig {

    @Resource
    private PsychologyAppDocumentLoader psychologyAppDocumentLoader;

    /**
     * 初始化基于PostgreSQL的向量数据库 Bean
     *
     * @param jdbcTemplate
     * @param dashscopeEmbeddingModel
     * @return
     */
    @Bean
    public VectorStore pgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
        PgVectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1536)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(true)
                .schemaName("public")
                .vectorTableName("pg_vector_store")
                .maxDocumentBatchSize(10000)
                .build();
        return vectorStore;
    }

}