package com.platncare.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static SharedPreferences prefs;
    private String email;
    private String password;

    private final static String APPTOKEN_KEY = "com.platncare.app.apptoken";
    private final static String EMAIL_KEY = "com.platncare.app.email";
    private final static String PASSWORD_KEY = "com.platncare.app.password";

    public static void saveAppToken(String appToken, Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(APPTOKEN_KEY, appToken).apply();
    }

    public static String getAppToken(Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(APPTOKEN_KEY, "default");
    }
}
