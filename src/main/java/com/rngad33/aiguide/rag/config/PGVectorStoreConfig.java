package com.rngad33.aiguide.rag.config;

import com.rngad33.aiguide.rag.documentloader.LoveAppDocumentLoader;
import com.rngad33.aiguide.rag.documentloader.PsychologyAppDocumentLoader;
import com.rngad33.aiguide.utils.AiModelUtils.MyEmbeddingModel;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 * PG向量数据库配置
 */
@Configuration
public class PGVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private PsychologyAppDocumentLoader psychologyAppDocumentLoader;

    /**
     * 初始化基于PostgreSQL的向量数据库 Bean
     *
     * @param jdbcTemplate
     * @param embeddingModel
     * @return
     */
    @Bean("lovePgVectorStore")
    public VectorStore lovePgVectorStore(JdbcTemplate jdbcTemplate, MyEmbeddingModel embeddingModel) {
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(true)
                .schemaName("public")
                .vectorTableName("love_pg_vector_store")
                .dimensions(1536)
                .maxDocumentBatchSize(10000)
                .build();
        // 加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        vectorStore.add(documents);
        return vectorStore;
    }

    /**
     * 初始化基于PostgreSQL的向量数据库 Bean
     *
     * @param jdbcTemplate
     * @param embeddingModel
     * @return
     */
    @Bean("psychologyPgVectorStore")
    public VectorStore psychologyPgVectorStore(JdbcTemplate jdbcTemplate, MyEmbeddingModel embeddingModel) {
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(true)
                .schemaName("public")
                .vectorTableName("psychology_pg_vector_store")
                .dimensions(1536)
                .maxDocumentBatchSize(10000)
                .build();
        // 加载文档
        List<Document> documents = psychologyAppDocumentLoader.loadMarkdowns();
        vectorStore.add(documents);
        return vectorStore;
    }

}