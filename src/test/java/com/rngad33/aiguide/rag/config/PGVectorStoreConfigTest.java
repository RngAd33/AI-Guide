package com.rngad33.aiguide.rag.config;

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
    private VectorStore lovePgVectorStore;

    @Resource
    private VectorStore psychologyPgVectorStore;

    @Test
    void lovePgVectorStore() {
        List<Document> documentList = List.of(
                new Document("鱼皮的编程导航有什么用？学编程啊，做项目啊", Map.of("meta1", "meta1")),
                new Document("程序员鱼皮的原创项目教程 codefather.cn"),
                new Document("鱼皮这小伙子比较帅气", Map.of("meta2", "meta2"))
        );
        // 添加文档
        lovePgVectorStore.add(documentList);
        // 相似度查询
        List<Document> result = lovePgVectorStore.similaritySearch(SearchRequest.builder().query("怎么学编程啊？").topK(3).build());
    }

    @Test
    void psychologyPgVectorStore() {
        List<Document> documentList = List.of(
                new Document("我这心理咨询师有什么特点？很可爱啊", Map.of("meta1", "meta1")),
                new Document("你就说我可不可爱吧！"),
                new Document("我看你小子也是风韵犹存", Map.of("meta2", "meta2"))
        );
        // 添加文档
        psychologyPgVectorStore.add(documentList);
        // 相似度查询
        List<Document> result = psychologyPgVectorStore.similaritySearch(SearchRequest.builder().query("你有什么特点？").topK(3).build());
        Assertions.assertNotNull(result);
    }

}