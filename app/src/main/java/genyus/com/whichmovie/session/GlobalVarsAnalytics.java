package genyus.com.whichmovie.session;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by genyus on 10/09/15.
 */
public class GlobalVarsAnalytics {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    public static GoogleAnalytics analytics() {
        return analytics;
    }

    public static Tracker tracker() {
        return tracker;
    }
}
