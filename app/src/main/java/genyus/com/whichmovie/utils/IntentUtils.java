package genyus.com.whichmovie.utils;

import android.content.Context;
import android.content.Intent;
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
}
