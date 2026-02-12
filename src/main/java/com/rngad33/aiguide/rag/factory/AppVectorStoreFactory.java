package com.rngad33.aiguide.rag.factory;

import com.rngad33.aiguide.utils.AiModelUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mariadb.MariaDBVectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 向量数据库工厂
 */
@Component
public class AppVectorStoreFactory implements VectorStoreFactory {

    @Override
    public VectorStore getVectorStore(AiModelUtils.MyEmbeddingModel embeddingModel, List<Document> documents) {
        return null;
    }

    public static VectorStore getVectorStore(JdbcTemplate jdbcTemplate, AiModelUtils.MyEmbeddingModel embeddingModel,
            String tableName, MariaDBVectorStore.MariaDBDistanceType distanceType) {
        return MariaDBVectorStore.builder(jdbcTemplate, embeddingModel)
                .distanceType(distanceType)
                .initializeSchema(true)
                .vectorTableName(tableName)
                .dimensions(1536)
                .maxDocumentBatchSize(10000)
                .build();
    }

}