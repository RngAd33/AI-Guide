package com.rngad33.aiguide.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 网页抓取工具
 */
public class WebScrapingTool {

    /**
     * Jsoup抓取
     *
     * @param url
     * @return
     */
    @Tool(description = "Scrape the content of a web page")
    public String doScrape(@ToolParam(description = "URL of thw web page to sarape") String url
    ) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.html();
        } catch (Exception e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }

}