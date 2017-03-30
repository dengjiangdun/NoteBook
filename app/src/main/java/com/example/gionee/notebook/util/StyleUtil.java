package com.example.gionee.notebook.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


/**
 * Created by johndon on 3/9/17.
 */

public  class StyleUtil {
    public static int  mTheme;
    private static final String KEY_GET_STYLE = "style";
    private static final String KEY_GET_THEME = "theme";
    public static void  changeTheme(Activity activity ,int theme){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(KEY_GET_STYLE,Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_GET_THEME,theme);
        editor.commit();
        Intent mIntent = new Intent(activity, activity.getClass());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(mIntent);
        activity.overridePendingTransition(0,0);
        activity.finish();

    }

    public static void  createTheme(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(KEY_GET_STYLE, Context.MODE_APPEND);
        mTheme = sharedPreferences.getInt(KEY_GET_THEME,-1);
        if (mTheme != -1){
            activity.setTheme(mTheme);
        }
    }

    public static int getTheme(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(KEY_GET_STYLE, Context.MODE_APPEND);
        mTheme = sharedPreferences.getInt(KEY_GET_THEME,-1);
        return mTheme;
    }
}
