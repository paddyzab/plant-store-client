package com.platncare.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static SharedPreferences prefs;

    private final static String APPTOKEN_KEY = "com.platncare.app.apptoken";
    private final static String EMAIL_KEY = "com.platncare.app.email";
    private final static String PASSWORD_KEY = "com.platncare.app.password";

    public static void saveAppToken(String appToken, Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(APPTOKEN_KEY, appToken).apply();
    }

    public static String getAppToken(Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(APPTOKEN_KEY, "");
    }

    public static void saveEmail(String email,  Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(EMAIL_KEY, email).apply();
    }

    public static String getEmail(Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(EMAIL_KEY, "");
    }

    public static void savePassword(String password,  Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(PASSWORD_KEY, password).apply();
    }

    public static String getPassword(Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(PASSWORD_KEY, "");
    }

    public static void clearAllPreferences(Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
