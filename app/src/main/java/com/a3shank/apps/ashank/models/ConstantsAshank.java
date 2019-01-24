package com.a3shank.apps.ashank.models;

import com.a3shank.apps.ashank.R;

import java.text.SimpleDateFormat;

/**
 * Created by Mahmoud Sadek on 1/4/2017.
 */

public class ConstantsAshank {
    public static final String USER_ID = "user_id";
    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_LOCATION_CLIENTS = "clients";
    public static final String FIREBASE_LOCATION_ITEMS_CLIENTS = "items_client";
    public static final String FIREBASE_LOCATION_MESSAGES = "messages";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final int[][] MAIN_CATEGORIES = {
            {R.string.resturant, R.string.cafe},
            {R.string.hospital, R.string.doctor, R.string.lab, R.string.scan},
            {R.string.women, R.string.men, R.string.baby},
            {R.string.accessories},
            {R.string.beauty_centers},
            {R.string.mechanical, R.string.electrical, R.string.srogy, R.string.car_washing, R.string.garage},
            {R.string.supermarket},
            {R.string.women_shoes, R.string.men_shoes},
            {R.string.furniture, R.string.electronics},
            {R.string.cam},
            {R.string.gym},
            {R.string.others}};
    public static final String[][] CATEGORIES_NAME = {
            {"Restaurant", "Cafe"},
            {"Hospital", "Doctor", "Lab", "Scan"},
            {"Women", "Men", "Baby"},
            {"Accessories"},
            {"Beauty Centers"},
            {"Mechanical", "Electrical", "EL-Srogy", "Car Washing", "Garage"},
            {"Supermarket"},
            {"Women Shoes", "Men Shoes"},
            {"Furniture", "Electronics Machine"},
            {"Camera and Studio"},
            {"GYM"},
            {"Others"}};
/*
    public static final String[] CATEGORY_NAME =
            {"Restaurant", "Cafe", "Hospital", "Doctor", "Lab", "Scan", "Women", "Men", "Baby", "Accessories",
                    "Beauty Centers", "Mechanical", "Electrical", "EL-Srogy", "Car Washing", "Garage",
                    "Supermarket", "Women Shoes", "Men Shoes", "Furniture", "Electronics Machine", "Camera and Studio",
                    "GYM", "Others"};
*/

    public static final String SENT = "sent";
    public static final String RECIVED = "recived";
    public static final String FIREBASE_LOCATION_HISTORY = "history";
    public static final String FIREBASE_LOCATION_FREECODES = "freeCodes";
    public static final String LANG = "lang";
    public static final String FIREBASE_ADVERTIS = "advertisments";
    public static String CURRENT_USER_ID = "";
    public static String CURRENT_USER_NAME = "";
    public static boolean FREECODE = false;
    public static boolean langCheck1 = true;
    public static boolean langCheck2 = true;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
