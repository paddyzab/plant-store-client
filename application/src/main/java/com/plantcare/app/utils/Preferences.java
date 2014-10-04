package com.plantcare.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static SharedPreferences prefs;

    private final static String APPTOKEN_KEY = "com.plantcare.app.apptoken";

    public static void saveAppToken(String appToken, Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(APPTOKEN_KEY, appToken).apply();
    }

    public static String getAppToken(Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(APPTOKEN_KEY, "");
    }

    public static void clearAllPreferences(Context context) {
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
