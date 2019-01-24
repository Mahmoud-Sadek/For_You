package com.a3shank.apps.ashank.models;

/**
 * Created by Mahmoud Sadek on 1/4/2017.
 */

public class User {

    String firstName,  lastName, email, gender, profileImage, birthday;

    public User(String firstName, String lastName, String email, String gender, String profileImage, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.profileImage = profileImage;
        this.birthday = birthday;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }
}
