package com.rngad33.aiguide.tools;

import com.rngad33.aiguide.model.entity.SearchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class WebSearchToolTest {

    @Value("${search.api-key}")
    private String searchApiKey;

    @Test
    void doSearchRaw() throws IOException {
        String query = "Java最新特性";
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String result = webSearchTool.doSearchRaw(query, 5);
        Assertions.assertNotNull(result);
    }

    @Test
    void doSearch() throws IOException {
        String query = "Java最新特性";
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        List<SearchResult> results = webSearchTool.doSearch(query, 5);
        Assertions.assertNotNull(results);
    }

}