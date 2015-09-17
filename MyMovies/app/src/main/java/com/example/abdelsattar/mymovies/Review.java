package com.example.abdelsattar.mymovies;

/**
 * Created by Mohammed on 9/17/2015.
 */
public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public  Review(){

    }
    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public Review(String id, String author, String content , String url) {

        this.id = id;
        this.url = url;
        this.author = author;
        this.content = content;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
