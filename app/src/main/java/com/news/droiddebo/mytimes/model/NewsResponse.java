package com.news.droiddebo.mytimes.model;

import java.util.List;

public class NewsResponse {
    private String status;
    private int totalResults;
    private List<ArticleStructure> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<ArticleStructure> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleStructure> articles) {
        this.articles = articles;
    }
}
