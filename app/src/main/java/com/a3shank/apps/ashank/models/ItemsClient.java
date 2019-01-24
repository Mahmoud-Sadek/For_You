package com.a3shank.apps.ashank.models;

/**
 * Created by Mahmoud Sadek on 1/18/2017.
 */

public class ItemsClient {
    String itemName;
    String description;
    String itemClientId;
    String itemPoto;

    public ItemsClient(String itemName, String description, String itemClientId, String itemPoto) {
        this.itemName = itemName;
        this.description = description;
        this.itemClientId = itemClientId;
        this.itemPoto = itemPoto;
    }

    public ItemsClient() {
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemClientId() {
        return itemClientId;
    }

    public String getDescription() {
        return description;
    }

    public String getItemPoto() {
        return itemPoto;
    }
}
