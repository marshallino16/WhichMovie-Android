package genyus.com.whichmovie.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import genyus.com.whichmovie.session.GlobalVarsAnalytics;

/**
 * Created by genyus on 04/09/15.
 */
public class AnalyticsEventUtils {

    public static void sendEvent(String screenName) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null){
            tracker.setScreenName(screenName);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }

    }

    public static void sendEventAction(String name, String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(name)
                    .setAction(action)
                    .build());
        }
    }

}
