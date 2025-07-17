package com.rngad33.aiguide.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 多查询扩展器
 */
@Component
public class MyMultiQueryExpander {

    private final ChatClient.Builder chatClientBuilder;

    public MyMultiQueryExpander(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel) {
        this.chatClientBuilder = ChatClient.builder(ollamaChatModel);
    }

    public List<Query> expand(String query) {
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = queryExpander.expand(new Query(query));
        return queries;
    }

}