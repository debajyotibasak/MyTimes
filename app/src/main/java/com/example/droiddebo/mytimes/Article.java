package com.example.droiddebo.mytimes;

public class Article {
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;


    public String getAuthor(){
        return author;
    }

    public String getTitle(){
        return title;
    }

//    public void setTitle(String title) { this.title = title; }

    public String getDescription(){
        return description;
    }

    public String getUrl(){
        return url;
    }

    public String getUrlToImage(){
        return urlToImage;
    }

//    public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }

    public String getPublishedAt(){
        return publishedAt;
    }
}
