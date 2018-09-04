package com.enhan.sabina.firebaseapp.model;

public class Article {
    private String article_content;
    private String article_id;
    private String article_tag;
    private String author;
    private String created_time;
    private String article_title;
    private String author_article_tag;

    public Article() {

    }

    public Article(String content, String title ,String tag,String author,String createdTime,String aatag ){
        article_content = content;
        article_title = title;
        article_tag = tag;
        this.author = author;
        created_time = createdTime;
        author_article_tag = aatag;
    }

    public String getArticle_content() {
        return article_content;
    }

    public String getAuthor_article_tag() {
        return author_article_tag;
    }

    public void setAuthor_article_tag(String author_article_tag) {
        this.author_article_tag = author_article_tag;
    }

    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public String getArticle_id() {
        return article_id;
    }

    public String getArticle_title() {
        return article_title;
    }
    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_tag() {
        return article_tag;
    }

    public void setArticle_tag(String article_tag) {
        this.article_tag = article_tag;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }
}
