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

@SpringBootTest
class PGVectorStoreConfigTest {

    @Resource
    private VectorStore pgVectorStore;

    @Test
    void pgVectorStore() {
        List<Document> documentList = List.of(
                new Document("我这心理咨询师有什么特点？很可爱啊", Map.of("meta1", "meta2")),
                new Document("你就说我可不可爱吧！"),
                new Document("我看你小子也是风韵犹存", Map.of("meta1", "meta2"))
        );
        // 添加文档
        pgVectorStore.add(documentList);
        // 相似度查询
        List<Document> result = pgVectorStore.similaritySearch(SearchRequest.builder().query("你有什么特点？").topK(3).build());
        Assertions.assertNotNull(result);
    }

}