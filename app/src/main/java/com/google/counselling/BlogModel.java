package com.google.counselling;

public class BlogModel {

    String title, link;

    public BlogModel() {
    }

    public BlogModel( String title, String link) {
        //this.blogId = blogId;
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
