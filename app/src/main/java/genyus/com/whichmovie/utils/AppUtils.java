package genyus.com.whichmovie.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import genyus.com.whichmovie.BuildConfig;
import genyus.com.whichmovie.service.NotifyService;

/**
 * Created by GENyUS on 02/02/16.
 */
public class AppUtils {

    public final static String getPlayStoreCurrentVersion(Context context){
        String newVersion = null;
        try {
            newVersion = Jsoup
                    .connect(
                            "https://play.google.com/store/apps/details?id="
                                    + context.getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent(
                            "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get()
                    .select("div[itemprop=softwareVersion]").first()
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(genyus.com.whichmovie.classes.Log.TAG, "Play Store version = " + newVersion);
        return newVersion;
    }

    public final static boolean isAnUpdateAvailable(Context context){
        String playStoreVersion = getPlayStoreCurrentVersion(context);
        String currentVersion = BuildConfig.VERSION_NAME;
        if(null != playStoreVersion && playStoreVersion.equals(currentVersion)){
            return false;
        } else {
            return true;
        }
    }

    public final static boolean isDeviceInFrench(){
        String language = Locale.getDefault().getDisplayLanguage();
        Log.w(genyus.com.whichmovie.classes.Log.TAG, "langage = " + language);
        if(language.equals("français")){
            return true;
        } else {
            return false;
        }
    }

    public final static void configureNotification(Context context){
        Intent myIntent = new Intent(context, NotifyService.class);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 7);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24 , pendingIntent);
    }
}
