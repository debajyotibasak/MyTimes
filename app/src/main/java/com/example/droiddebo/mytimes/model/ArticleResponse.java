package com.example.droiddebo.mytimes.model;


import java.util.List;

/*
** POJO class to get the list of Articles.
**/
public class ArticleResponse {
    private String status;
    private String source;
    private String sortBy;
    private List<Article> articles;

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getSource(){
        return source;
    }

    public void setSource(String source){
        this.source = source;
    }

    public String getSortBy(){
        return sortBy;
    }

    public void setSortBy(String sortBy){
        this.sortBy = sortBy;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles){
        this.articles = articles;
    }
}
