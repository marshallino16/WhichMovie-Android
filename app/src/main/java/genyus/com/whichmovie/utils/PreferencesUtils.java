package genyus.com.whichmovie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import genyus.com.whichmovie.BuildConfig;

/**
 * Created by genyus on 29/11/15.
 */
public class PreferencesUtils {

    private static final String MY_PREFS_NAME = "MOVIE_PREFS";
    public static final String KEY_DEFAULT_CATEGORY = "default_category";
    public static final String KEY_VERSION_CODE = "version_code";
    public static final int DEFAULT_CATEGORY = 28;

    public static void setPreference(Context context, String key, int value){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static int getPreference(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        int restoredPref = prefs.getInt(key, -1);
        return restoredPref;
    }

    public static int getDefaultCategory(Context context){
        int restoredValue = getPreference(context, KEY_DEFAULT_CATEGORY);
        if(-1 == restoredValue){
            return DEFAULT_CATEGORY;
        } else {
            return restoredValue;
        }
    }

    public static int getLastAppVersionCode(Context context){
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        int restoredPref = prefs.getInt(KEY_VERSION_CODE, 1);
        return restoredPref;
    }

    public static void setLastAppVersionCode(Context context){
        int versionCode = BuildConfig.VERSION_CODE;
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_VERSION_CODE, versionCode);
        editor.commit();
    }
}
