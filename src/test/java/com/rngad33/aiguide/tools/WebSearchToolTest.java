package com.rngad33.aiguide.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSearchToolTest {

    @Resource
    private WebSearchTool webSearchTool;

    @Test
    void searchRaw() throws IOException {
        String query = "Java最新特性";
        String result = webSearchTool.searchRaw(query, 5);
        Assertions.assertNotNull(result);
    }

    @Test
    void search() throws IOException {
        String query = "Java最新特性";
        List<WebSearchTool.SearchResult> results = webSearchTool.search(query, 5);
        Assertions.assertNotNull(results);
    }

}