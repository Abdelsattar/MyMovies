package com.example.abdelsattar.mymovies;

/**
 * Created by Mohammed on 9/17/2015.
 */
public class Video {
    private String name;
    private String site;
    private String type;
    private String Url;
    private String id;

    public Video(){

    }
    public Video(String name, String site, String type, String Url, String id) {
        this.name = name;
        this.site = site;
        this.type = type;
        this.Url=Url;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {

        return Url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public void setId(String id) {
        this.id = id;
    }
}
