package com.a3shank.apps.ashank.models;

/**
 * Created by Mahmoud Sadek on 6/14/2017.
 */
public class Advertise {
    String key, url;

    public Advertise() {
    }

    public Advertise(String key, String url) {
        this.key = key;
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }
}
