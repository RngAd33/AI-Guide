package com.rngad33.aiguide.rag.factory;

import com.rngad33.aiguide.utils.AiModelUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

/**
 * 向量数据库工厂接口
 */
public interface VectorStoreFactory {

    VectorStore getVectorStore(AiModelUtils.MyEmbeddingModel embeddingModel, List<Document> documents);

}