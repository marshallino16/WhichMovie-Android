package genyus.com.whichmovie;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import genyus.com.whichmovie.classes.Analytics;
import genyus.com.whichmovie.session.GlobalVarsAnalytics;
import genyus.com.whichmovie.utils.WaveHelper;
import genyus.com.whichmovie.view.FlakeView;
import genyus.com.whichmovie.view.WaveView;

@EActivity(R.layout.activity_loading)
public class LoadingActivity extends AppCompatActivity {

    private final static String FONT_FLAT = "fonts/Sertig.otf";
    private WaveHelper mWaveHelper;
    private FlakeView flakeView;

    @ViewById(R.id.flakeContainer)
    LinearLayout flakeContainer;

    @ViewById(R.id.wave)
    WaveView waveView;

    @ViewById(R.id.app_name)
    TextView appName;

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

    @AfterViews
    protected void afterViews() {
        int mBorderColor = Color.parseColor("#FFFFFF");
        int mBorderWidth = 0;

        mWaveHelper = new WaveHelper(waveView);
        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
        waveView.setBorder(mBorderWidth, mBorderColor);
        mWaveHelper.start();

        Typeface font = Typeface.createFromAsset(this.getAssets(), FONT_FLAT);
        appName.setTypeface(font);

        flakeView = new FlakeView(this);
        flakeContainer.addView(flakeView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
        flakeView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();
    }
}
