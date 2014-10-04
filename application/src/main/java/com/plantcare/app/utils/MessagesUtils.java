package com.plantcare.app.utils;

import android.content.Context;
import android.widget.Toast;

public class MessagesUtils {

    public static void displayToastMessage(int stringId, Context context) {
        Toast.makeText(context, context.getString(stringId), Toast.LENGTH_LONG).show();
    }

    public static void displayToastMessage(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
