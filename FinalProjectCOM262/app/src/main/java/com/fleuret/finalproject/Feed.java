package com.fleuret.finalproject;

/**
 * Created by James Fleuret on 10/7/2015.
 */
public class Feed {
    private String title;
    private String url;
    private int id;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Feed(String _title, String _url, int _id){
        setTitle(_title);
        setUrl(_url);
        setId(_id);
    }
}
