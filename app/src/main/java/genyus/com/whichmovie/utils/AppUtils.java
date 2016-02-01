package genyus.com.whichmovie.utils;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.IOException;

import genyus.com.whichmovie.BuildConfig;

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

}
