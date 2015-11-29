package genyus.com.whichmovie;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import genyus.com.whichmovie.classes.Analytics;
import genyus.com.whichmovie.session.GlobalVarsAnalytics;
import genyus.com.whichmovie.task.listener.OnConfigurationListener;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.utils.WaveHelper;
import genyus.com.whichmovie.view.FlakeView;
import genyus.com.whichmovie.view.WaveView;

@EActivity(R.layout.activity_loading)
public class LoadingActivity extends AppCompatActivity implements OnConfigurationListener{

    private final static String FONT_FLAT = "fonts/Marta_Regular.otf";
    private WaveHelper mWaveHelper;
    private FlakeView flakeView;

    @ViewById(R.id.flakeContainer)
    LinearLayout flakeContainer;

    @ViewById(R.id.wave)
    WaveView waveView;

    @ViewById(R.id.app_name)
    TextView appName;

    @ViewById(R.id.accroche)
    TextView accroche;

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

        Typeface font = Typeface.createFromAsset(this.getAssets(), FONT_FLAT);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_in_from_bottom);

        appName.setTypeface(font);
        accroche.startAnimation(fadeIn);
        fadeIn.start();

        /**
         * Christmas templates
         */
        //flakeView = new FlakeView(this);
        //flakeContainer.addView(flakeView);

        initGlobalVars();
        goToMainActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
        //flakeView.pause();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
        //flakeView.resume();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void goToNextActivity() {
        MainActivity_.intent(LoadingActivity.this).start();
    }

    private void initGlobalVars(){
        new Thread() {
            public void run() {
                RequestManager.getInstance(LoadingActivity.this).getConfigurations(LoadingActivity.this);
            }
        }.start();
    }

    private void prefetchMovies(){

    }

    private void goToMainActivity(){
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goToNextActivity();
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    public void OnConfigurationGet() {
        prefetchMovies();
    }

    @Override
    public void OnConfigurationFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting configuration : " + reason);
    }
}
