package com.a3shank.apps.ashank.models;

/**
 * Created by Mahmoud Sadek on 3/19/2017.
 */
public class Categorys {
    String item_name;
    int img;
    String name;

    public Categorys(String item_name, int img, String name) {
        this.item_name = item_name;
        this.img = img;
        this.name = name;
    }

    public String getItem_name() {
        return item_name;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }
}
