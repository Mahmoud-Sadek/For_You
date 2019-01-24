package com.a3shank.apps.ashank.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Mahmoud Sadek on 1/12/2017.
 */
public class Client {
    String firstName, lastName, email, phone, resume, desc, clientId, profileImage, catagorie;
    int activeCodeNum;

    double lat, lang;
    HashMap<String, Object> timestampCreated = new HashMap<>();

    public Client(String firstName, String lastName, String email, String phone, String resume, String shortDesc, String clientId, String catagorie, String profileImage,
                  double lat, double lang, int activeCodeNum, HashMap<String, Object> timestampCreated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.resume = resume;
        this.desc = shortDesc;
        this.clientId = clientId;
        this.profileImage = profileImage;
        this.lat = lat;
        this.lang = lang;
        this.activeCodeNum = activeCodeNum;
        this.timestampCreated = timestampCreated;
        HashMap<String, Object> timestampNowObject = new HashMap<String, Object>();
        timestampNowObject.put(ConstantsAshank.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.catagorie = catagorie;
    }

    public Client() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getResume() {
        return resume;
    }

    public String getDesc() {
        return desc;
    }

    public String getClientId() {
        return clientId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getCatagorie() {
        return catagorie;
    }

    public int getActiveCodeNum() {
        return activeCodeNum;
    }

    public double getLat() {
        return lat;
    }

    public double getLang() {
        return lang;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }

    @Exclude
    public long getTimestampCreatedLong() {
        return (long) timestampCreated.get("timestamp");
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setCatagorie(String catagorie) {
        this.catagorie = catagorie;
    }

    public void setActiveCodeNum(int activeCodeNum) {
        this.activeCodeNum = activeCodeNum;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public void setTimestampCreated(HashMap<String, Object> timestampCreated) {
        this.timestampCreated = timestampCreated;
    }
}
