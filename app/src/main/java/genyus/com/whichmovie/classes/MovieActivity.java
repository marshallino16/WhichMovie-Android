package genyus.com.whichmovie.classes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;

import genyus.com.whichmovie.session.GlobalVarsAnalytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by GENyUS on 01/02/16.
 */
public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fabrics
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
        //Analytics
        GlobalVarsAnalytics.analytics = GoogleAnalytics.getInstance(this);
        GlobalVarsAnalytics.analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        GlobalVarsAnalytics.tracker = GlobalVarsAnalytics.analytics.newTracker(Analytics.PROPERTY_ID);
        GlobalVarsAnalytics.tracker.enableExceptionReporting(true);
        GlobalVarsAnalytics.tracker.enableAutoActivityTracking(false);
    }
}
