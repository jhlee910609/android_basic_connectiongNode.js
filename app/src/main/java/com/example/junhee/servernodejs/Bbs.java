package com.example.junhee.servernodejs;

/**
 * Created by JunHee on 2017. 7. 25..
 */

public class Bbs {

    private String id;
    private String date;
    private String content;
    private String author;
    private String title;

    public Bbs(String title, String author, String content) {
        this.content = content;
        this.title = title;
        this.author = author;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
