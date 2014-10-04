package com.plantcare.app.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentUtils {

    public static void setFragment(Activity activity, Fragment fragment, int id) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fr = fm.findFragmentById(id);

        if (fr != null) {
            ft.remove(fr);
        }

        ft.replace(id, fragment);
        ft.commit();
    }

    public static void removeFragment(Activity activity, int id) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fr = fm.findFragmentById(id);

        if (fr != null) {
            ft.remove(fr);
        }

        ft.commit();
    }

}
