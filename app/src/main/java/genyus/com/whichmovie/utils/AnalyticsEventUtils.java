package genyus.com.whichmovie.utils;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import genyus.com.whichmovie.session.GlobalVarsAnalytics;

/**
 * Created by genyus on 04/09/15.
 */
public class AnalyticsEventUtils {

    public static void sendEvent(Activity context, String screenName) {
        Tracker tracker =  GlobalVarsAnalytics.tracker();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void sendEventAction(Activity context, String screenName, String name, String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(name)
                .setAction(action)
                .build());
    }

}
