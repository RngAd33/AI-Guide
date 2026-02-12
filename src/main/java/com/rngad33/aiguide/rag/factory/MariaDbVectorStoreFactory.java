package com.rngad33.aiguide.rag.factory;

import com.rngad33.aiguide.utils.AiModelUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MariaDbVectorStoreFactory implements VectorStoreFactory {
    @Override
    public VectorStore getVectorStore(AiModelUtils.MyEmbeddingModel embeddingModel, List<Document> documents) {
        return null;
    }
}