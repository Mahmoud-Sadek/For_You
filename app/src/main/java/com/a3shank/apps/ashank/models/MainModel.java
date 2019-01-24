package com.a3shank.apps.ashank.models;

/**
 * Created by Mahmoud Sadek on 1/28/2017.
 */

public class MainModel {
    int photo;
    String categorie;

    public MainModel(int photo, String categorie) {
        this.photo = photo;
        this.categorie = categorie;
    }

    public int getPhoto() {
        return photo;
    }

    public String getCategorie() {
        return categorie;
    }
}
