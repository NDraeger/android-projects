package com.ndraeger.storify.network;

import com.ndraeger.storify.news.Article;

import java.util.List;

public class ArticlesResponse {

    private String status;
    private Integer totalResults;
    private List<Article> articles;

    public ArticlesResponse(String status, Integer totalResults, List<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
