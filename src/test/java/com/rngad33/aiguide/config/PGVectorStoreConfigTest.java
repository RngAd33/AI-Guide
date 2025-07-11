package com.rngad33.aiguide.config;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PGVectorStoreConfigTest {

    @Resource
    private VectorStore pgVectorStore;

    @Test
    void pgVectorStore() {
        List<Document> documentList = List.of(
                new Document("我这心理咨询师有什么特点？和善啊", Map.of("meta1", "meta2")),
                new Document("2"),
                new Document("3", Map.of("meta1", "meta2"))
        );
        // 添加文档
        pgVectorStore.add(documentList);
        // 相似度查询
        List<Document> result = pgVectorStore.similaritySearch(SearchRequest.builder().query("").topK(3).build());
        Assertions.assertNotNull(result);
    }

}