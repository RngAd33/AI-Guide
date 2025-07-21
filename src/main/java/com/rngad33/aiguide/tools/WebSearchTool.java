package com.rngad33.aiguide.tools;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 联网搜索工具
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSearchTool {

    @Value("${search.api-key}")
    private String jinaApiKey;

    private static final String JINA_API_BASE_URL = "https://api.jina.ai";
    private static final String SEARCH_ENDPOINT = "/v1/search";

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 执行搜索并返回原始JSON响应
     */
    public String searchRaw(String query, int limit) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("query", query);
        requestBody.put("limit", limit);

        Request request = new Request.Builder()
                .url(JINA_API_BASE_URL + SEARCH_ENDPOINT)
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.parse("application/json")
                ))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + jinaApiKey)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("搜索请求失败: {}", response);
                throw new IOException("Unexpected code " + response + ", body: " +
                        (response.body() != null ? response.body().string() : "null"));
            }
            return response.body().string();
        }
    }

    /**
     * 执行搜索并返回解析后的结果
     */
    public List<SearchResult> search(String query, int limit) throws IOException {
        return parseSearchResults(searchRaw(query, limit));
    }

    private List<SearchResult> parseSearchResults(String jsonResponse) {
        List<SearchResult> results = new ArrayList<>();
        JSONArray hits = new JSONObject(jsonResponse).getJSONArray("hits");

        for (int i = 0; i < hits.length(); i++) {
            JSONObject hit = hits.getJSONObject(i);
            JSONObject document = hit.getJSONObject("document");

            results.add(SearchResult.builder()
                    .id(hit.optString("id"))
                    .score(hit.optDouble("score"))
                    .title(document.optString("title"))
                    .content(document.optString("content"))
                    .url(document.optString("url"))
                    .metadata(document.optJSONObject("metadata"))
                    .build());
        }
        return results;
    }

    @Data
    @Builder
    public static class SearchResult {
        private String id;
        private double score;
        private String title;
        private String content;
        private String url;
        private JSONObject metadata;

        @Override
        public String toString() {
            return String.format(
                    "SearchResult{id='%s', score=%.2f, title='%s', content='%s', url='%s'}",
                    id, score, title,
                    content != null ? content.substring(0, Math.min(50, content.length())) + "..." : "null",
                    url
            );
        }
    }

    // 示例用法
    public static void main(String[] args) {
        WebSearchTool searcher = new WebSearchTool();
        try {
            List<SearchResult> results = searcher.search("Java最新特性", 3);
            log.info("搜索结果: {}", results);
        } catch (IOException e) {
            log.error("搜索失败", e);
        }
    }

}