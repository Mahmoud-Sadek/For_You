package com.a3shank.apps.ashank.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Sadokey on 12/22/2016.
 */

public class ChatModel {
    String msgText;
    String msgSenderID;
    String msgSenderName;
//    HashMap<String, Object> timestampCreated = new HashMap<>();

    public ChatModel(String msgText, String msgSenderID, String msgSenderName) {
        this.msgText = msgText;
////        this.timestampCreated = timestampCreated;
//        HashMap<String, Object> timestampNowObject = new HashMap<String, Object>();
//        timestampNowObject.put(ConstantsAshank.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.msgSenderID = msgSenderID;
        this.msgSenderName = msgSenderName;
    }

    public ChatModel() {
    }

    public String getMsgText() {
        return msgText;
    }

    public String getMsgSenderID() {
        return msgSenderID;
    }

    public String getMsgSenderName() {
        return msgSenderName;
    }
}
