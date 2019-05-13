package com.example.offlinemaps;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by amardeep on 11/3/2017.
 */

public class ScreenUtils {

    //static method to get screen width
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    //static method to get screen height
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}