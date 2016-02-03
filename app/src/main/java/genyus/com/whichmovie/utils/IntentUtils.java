package genyus.com.whichmovie.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by anthony on 1/28/16.
 */
public class IntentUtils {

    public final static void searchOnNetflix(Context mContext, String movieTitle) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEARCH);
            intent.setClassName("com.netflix.mediaclient", "com.netflix.mediaclient.ui.search.SearchActivity");
            intent.putExtra("query", movieTitle);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "First, install Netflix App :)", Toast.LENGTH_SHORT).show();
        }
    }

    public final static void searchOnGooglePlay(Context mContext, String link) {
        final Uri uri = Uri.parse(link);
        final Intent rateAppIntent = new Intent(
                Intent.ACTION_VIEW, uri);

        if (mContext.getPackageManager().queryIntentActivities(
                rateAppIntent, 0).size() > 0) {
            mContext.startActivity(rateAppIntent);
        } else {
            Toast.makeText(mContext, "First, install Google Play Movie App :)", Toast.LENGTH_SHORT).show();
        }
    }

    public final static void searchOnVudu(Context mContext, String link) {
        final Uri uri = Uri.parse(link);
        final Intent rateAppIntent = new Intent(
                Intent.ACTION_VIEW, uri);

        if (mContext.getPackageManager().queryIntentActivities(
                rateAppIntent, 0).size() > 0) {
            mContext.startActivity(rateAppIntent);
        } else {
            Toast.makeText(mContext, "First, install Vudu App :)", Toast.LENGTH_SHORT).show();
        }
    }
}
