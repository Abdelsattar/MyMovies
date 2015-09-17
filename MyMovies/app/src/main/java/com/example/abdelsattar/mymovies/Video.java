package com.example.abdelsattar.mymovies;

/**
 * Created by Mohammed on 9/17/2015.
 */
public class Video {
    private String name;
    private String site;
    private String type;
    private String key;
    private String id;


    public Video(String name, String site, String type, String key, String id) {
        this.name = name;
        this.site = site;
        this.type = type;
        this.key = key;
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

    public String getKey() {
        return key;
    }
}
