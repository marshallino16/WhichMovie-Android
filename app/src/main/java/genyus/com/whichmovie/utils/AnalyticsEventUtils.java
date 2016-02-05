package genyus.com.whichmovie.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import genyus.com.whichmovie.classes.Analytics;
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

    public static void sendCategoryAction(String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(Analytics.EVENT.CATEGORY.toString())
                    .setAction(action)
                    .build());
        }
    }

    public static void sendClickAction(String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(Analytics.EVENT.ELEMENT_CLICK.toString())
                    .setAction(action)
                    .build());
        }
    }

    public static void sendRateAction(String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(Analytics.EVENT.RATE.toString())
                    .setAction(action)
                    .build());
        }
    }

    public static void sendSaveAction(String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(Analytics.EVENT.SAVE.toString())
                    .setAction(action)
                    .build());
        }
    }

    public static void sendScreenEnterAction(String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(Analytics.EVENT.SCREEN_ENTER.toString())
                    .setAction(action)
                    .build());
        }
    }

    public static void sendScreenQuitAction(String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(Analytics.EVENT.SCREEN_QUIT.toString())
                    .setAction(action)
                    .build());
        }
    }

    public static void sendShareAction(String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(Analytics.EVENT.SHARE.toString())
                    .setAction(action)
                    .build());
        }
    }

    public static void sendStreamAction(String action) {
        Tracker tracker = GlobalVarsAnalytics.tracker();
        if (tracker!=null) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(Analytics.EVENT.STREAM.toString())
                    .setAction(action)
                    .build());
        }
    }
}
