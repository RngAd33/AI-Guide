package com.rngad33.aiguide.tools;

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
    void searchRaw() throws IOException {
        String query = "Java最新特性";
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String result = webSearchTool.searchRaw(query, 5);
        Assertions.assertNotNull(result);
    }

    @Test
    void search() throws IOException {
        String query = "Java最新特性";
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        List<WebSearchTool.SearchResult> results = webSearchTool.search(query, 5);
        Assertions.assertNotNull(results);
    }

}