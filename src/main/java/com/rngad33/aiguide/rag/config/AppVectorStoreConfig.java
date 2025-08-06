package com.rngad33.aiguide.rag.config;

import com.rngad33.aiguide.exception.MyException;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.rag.custom.MyKeywordEnricher;
import com.rngad33.aiguide.rag.custom.MyTokenTextSplitter;
import com.rngad33.aiguide.rag.documentloader.LoveAppDocumentLoader;
import com.rngad33.aiguide.rag.documentloader.PsychologyAppDocumentLoader;
import com.rngad33.aiguide.utils.AiModelUtils.MyEmbeddingModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * 本地向量数据库配置
 */
@Slf4j
@Configuration
public class AppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private PsychologyAppDocumentLoader psychologyAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    /**
     * 初始化基于内存的向量数据库 Bean1
     *
     * @param embeddingModel
     * @return
     */
    @Bean("loveAppVectorStore")
    public VectorStore loveAppVectorStore(MyEmbeddingModel embeddingModel) {
        try {
            SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
            // 加载文档
            List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
            // 切割文档
            List<Document> splitDocuments = myTokenTextSplitter.splitDocuments(documents);
            // simpleVectorStore.add(splitDocuments);
            // 元信息增强
            List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(splitDocuments);
            simpleVectorStore.add(enrichedDocuments);
            return simpleVectorStore;
        } catch (RestClientException e) {
            log.error("LoveApp 嵌入式模型服务连接失败: {}", e.getMessage());
            throw new MyException(ErrorCodeEnum.USER_LOSE_ACTION);
        }
    }

    /**
     * 初始化基于内存的向量数据库 Bean2
     *
     * @param embeddingModel
     * @return
     */
    @Bean("psychologyAppVectorStore")
    public VectorStore psychologyAppVectorStore(MyEmbeddingModel embeddingModel) {
        try {
            SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
            // 加载文档
            List<Document> documents = psychologyAppDocumentLoader.loadMarkdowns();
            // 切割文档
            List<Document> splitDocuments = myTokenTextSplitter.splitDocuments(documents);
            // simpleVectorStore.add(splitDocuments);
            // 元信息增强
            List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(splitDocuments);
            simpleVectorStore.add(enrichedDocuments);
            return simpleVectorStore;
        } catch (RestClientException e) {
            log.error("PsychologyApp 嵌入式模型服务连接失败: {}", e.getMessage());
            throw new MyException(ErrorCodeEnum.USER_LOSE_ACTION);
        }
    }

}