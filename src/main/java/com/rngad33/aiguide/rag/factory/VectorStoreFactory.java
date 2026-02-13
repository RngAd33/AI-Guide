package com.rngad33.aiguide.rag.factory;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import com.rngad33.aiguide.constant.KnowledgeIndexConstant;
import com.rngad33.aiguide.exception.MyException;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.rag.custom.MyKeywordEnricher;
import com.rngad33.aiguide.rag.custom.MyTokenTextSplitter;
import com.rngad33.aiguide.utils.AiModelUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mariadb.MariaDBVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * 向量数据库工厂
 */
@Component
@Slf4j
public class VectorStoreFactory {

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;

    /**
     * 创建向量数据库（本地文档）
     *
     * @param embeddingModel
     * @param documents
     * @return
     */
    public VectorStore getVectorStore(AiModelUtils.MyEmbeddingModel embeddingModel, List<Document> documents, String index) {
        int i = 0;
        log.info("正在为{}应用载入文档知识库……", index);
        while (i < 5){
            try {
                SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
                // 切割文档
//            List<Document> splitDocuments = myTokenTextSplitter.splitDocuments(documents);
                List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);
                // 元信息增强
                List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(splitDocuments);
                // 检查数据合法性
                if (enrichedDocuments == null || enrichedDocuments.isEmpty()) {
                    log.warn("增强文档为空，跳过向量存储添加操作");
                    return simpleVectorStore;
                }
                // 添加到向量存储
                simpleVectorStore.add(enrichedDocuments);
                return simpleVectorStore;
            } catch (RestClientException e) {
                i += 1;
                log.warn("！嵌入式模型服务连接失败，正在重试第 {} 次……", i);
            }
        }
        throw new MyException(ErrorCodeEnum.USER_LOSE_ACTION, "！向量存储操作多次失败，请检查您的网络连接！");
    }

    /**
     * 创建向量数据库（MariaDB）
     *
     * @param jdbcTemplate
     * @param embeddingModel
     * @param tableName
     * @param distanceType
     * @return
     */
    public VectorStore getVectorStore(JdbcTemplate jdbcTemplate, AiModelUtils.MyEmbeddingModel embeddingModel,
                                      String tableName, MariaDBVectorStore.MariaDBDistanceType distanceType) {
        return MariaDBVectorStore.builder(jdbcTemplate, embeddingModel)
                .distanceType(distanceType)
                .initializeSchema(true)
                .vectorTableName(tableName)
                .dimensions(1536)
                .maxDocumentBatchSize(10000)
                .build();
    }

    /**
     * 创建向量数据库（云知识库）
     *
     * @param index
     * @return
     */
    public Advisor getVectorStore(String index) {
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(index)
                        .build());
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();
    }

}