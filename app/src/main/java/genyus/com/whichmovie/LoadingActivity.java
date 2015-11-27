package genyus.com.whichmovie;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;

import org.androidannotations.annotations.EActivity;

import genyus.com.whichmovie.classes.Analytics;
import genyus.com.whichmovie.session.GlobalVarsAnalytics;

@EActivity(R.layout.activity_loading)
public class LoadingActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //Analytics
        GlobalVarsAnalytics.analytics = GoogleAnalytics.getInstance(this);
        GlobalVarsAnalytics.analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        GlobalVarsAnalytics.tracker = GlobalVarsAnalytics.analytics.newTracker(Analytics.PROPERTY_ID);
        GlobalVarsAnalytics.tracker.enableExceptionReporting(true);
        GlobalVarsAnalytics.tracker.enableAutoActivityTracking(false);
    }
}
