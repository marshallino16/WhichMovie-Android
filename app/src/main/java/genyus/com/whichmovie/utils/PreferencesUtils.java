package genyus.com.whichmovie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import genyus.com.whichmovie.BuildConfig;

/**
 * Created by genyus on 29/11/15.
 */
public class PreferencesUtils {

    private static final String MY_PREFS_NAME = "MOVIE_PREFS";

    public static final String KEY_DEFAULT_CATEGORY = "default_category";
    public static final String KEY_VERSION_CODE = "version_code";
    public static final String KEY_FAVORITE_DATE = "date";
    public static final String KEY_FAVORITE_CATEGORY = "category";
    public static final String KEY_FAVORITE_ID = "id";
    public final static String KEY_RATE = "rate";
    public final static String KEY_PAGE = "page";

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

    public static void setStringPreference(Context context, String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringPreference(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String restoredPref = prefs.getString(key, null);
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

    public static void setRatePreference(Context c, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_RATE, value);
        editor.apply();
    }

    public static boolean getRatePreference(Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        boolean value = settings.getBoolean(KEY_RATE, false);
        return value;
    }

    public static void setPagePreference(Context c, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_PAGE, value);
        editor.apply();
    }

    public static int getPagePreference(Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        int value = settings.getInt(KEY_PAGE, 1);
        return value;
    }

    public static String getPromoCodeText(Context c){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        String prefEditText = sharedPreferences.getString("promo", "default");
        Log.d(genyus.com.whichmovie.classes.Log.TAG, "Promo = " + prefEditText);
        return prefEditText;
    }

    public static boolean isFirstRun(Context c){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        boolean prefEditText = sharedPreferences.getBoolean("firstrun", true);
        return prefEditText;
    }

    public static void setFirstRun(Context c){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstrun", false);
        editor.apply();
    }
}
