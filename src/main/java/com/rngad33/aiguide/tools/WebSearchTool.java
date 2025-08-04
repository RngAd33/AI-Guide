package com.rngad33.aiguide.tools;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rngad33.aiguide.common.SearchResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
     * 官方搜索方法
     *
     * @param query
     * @return
     * @throws IOException
     */
    public int doEasySearch(@ToolParam(description = "Search query keyword") String query
    ) throws IOException {
        try {
            URL url = new URL(JINA_SEARCH_URL + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + searchApiKey);
            connection.setRequestProperty("X-Respond-With", "no-content");
            int responseCode = connection.getResponseCode();
            log.info("Response Code: {}", responseCode);
            connection.disconnect();
            return responseCode;
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return -1;
        }
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
                .url(JINA_SEARCH_URL + query)
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
        try {
            // 安全解析为JSONObject
            JSONObject responseObj = JSONUtil.parseObj(jsonResponse, false);
            JSONArray hits = responseObj.getJSONArray("hits");

            List<SearchResult> results = new ArrayList<>();
            for (int i = 0; i < hits.size(); i++) {
                JSONObject hit = hits.getJSONObject(i);
                JSONObject document = hit.getJSONObject("document");
                // 使用optXXX方法避免空指针
                results.add(SearchResult.builder()
                        .id(hit.getStr("id", ""))
                        .score(hit.getDouble("score", 0.0))
                        .title(document.getStr("title", ""))
                        .content(document.getStr("content", ""))
                        .url(document.getStr("url", ""))
                        .metadata(document.getStr("metadata", "{}"))
                        .build());
            }
            return results;
        } catch (Exception e) {
            // 如果是数组格式或其他格式，记录错误并返回空列表
            // log.error("Invalid response format: {}", jsonResponse);
            log.error("Invalid response format");
            return Collections.emptyList();
        }
    }

}