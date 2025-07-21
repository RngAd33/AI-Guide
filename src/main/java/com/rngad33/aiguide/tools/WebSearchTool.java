package com.rngad33.aiguide.tools;

import com.rngad33.aiguide.model.entity.SearchResult;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 联网搜索工具（采用JINA引擎）
 */
@Slf4j
public class WebSearchTool {

    // 网页抓取
    private static final String JINA_READ_URL = "https://r.jina.ai/";

    // 关键词搜索
    private static final String JINA_SEARCH_URL = "https://s.jina.ai/?q=";

    private final String searchApiKey;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public WebSearchTool(String searchApiKey) {
        this.searchApiKey = searchApiKey;
    }

    /**
     * 执行搜索并返回解析后的最终结果
     */
    @Tool(description = "Search for information from Baidu Search Engine")
    public List<SearchResult> doSearch(@ToolParam(description = "Search query keyword")String query,
                                     @ToolParam(description = "Search limited tips") int limit
    ) throws IOException {
        return parseSearchResults(doSearchRaw(query, limit));
    }

    /**
     * 执行搜索并返回原始JSON响应
     *
     * @param query
     * @param limit
     * @return
     * @throws IOException
     */
    public String doSearchRaw(String query, int limit) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("query", query);
        requestBody.put("limit", limit);
        // 创建请求
        Request request = new Request.Builder()
                .url(JINA_SEARCH_URL + SEARCH_ENDPOINT)
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.parse("application/json")
                ))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + searchApiKey)
                .build();
        // 执行搜索
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
     * 解析搜索结果
     *
     * @param jsonResponse
     * @return
     */
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

}