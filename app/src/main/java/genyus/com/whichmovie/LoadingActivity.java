package genyus.com.whichmovie;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import genyus.com.whichmovie.animation.ExpandCollapse;
import genyus.com.whichmovie.classes.Analytics;
import genyus.com.whichmovie.session.GlobalVarsAnalytics;
import genyus.com.whichmovie.task.listener.OnCategoriesListener;
import genyus.com.whichmovie.task.listener.OnConfigurationListener;
import genyus.com.whichmovie.task.listener.OnMoviesListener;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.utils.WaveUtils;
import genyus.com.whichmovie.view.FlakeView;
import genyus.com.whichmovie.view.WaveView;

@EActivity(R.layout.activity_loading)
public class LoadingActivity extends AppCompatActivity implements OnConfigurationListener, OnCategoriesListener, OnMoviesListener{

    private final static String FONT_FLAT = "fonts/Ikaros Regular.otf";
    private WaveUtils mWaveHelper;
    private FlakeView flakeView;

    @ViewById(R.id.flakeContainer)
    LinearLayout flakeContainer;

    @ViewById(R.id.retry_container)
    RelativeLayout retryContainer;

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

        mWaveHelper = new WaveUtils(waveView);
        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
        waveView.setBorder(mBorderWidth, mBorderColor);

        Typeface font = Typeface.createFromAsset(this.getAssets(), FONT_FLAT);

        appName.setTypeface(font);

        /**
         * Christmas templates
         */
        //flakeView = new FlakeView(this);
        //flakeContainer.addView(flakeView);

        initGlobalVars();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
        //flakeView.pause();
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
        new Thread() {
            public void run() {
                RequestManager.getInstance(LoadingActivity.this).getMoviesFromCategory(LoadingActivity.this, LoadingActivity.this);
            }
        }.start();
    }

    private void prefetchCategories(){
        new Thread() {
            public void run() {
                RequestManager.getInstance(LoadingActivity.this).getAllCategories(LoadingActivity.this);
            }
        }.start();
    }

    @Override
    public void OnConfigurationGet() {
        prefetchCategories();
    }


    @Override
    public void OnCategoriesGet() {
        prefetchMovies();
    }

    @Override
    public void OnMoviesGet() {
        //Go to next activity
        try {
            Thread.sleep(2000);
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

    @Override
    public void OnCategoriesFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting categories : " + reason);
    }

    @Override
    public void OnConfigurationFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting configuration : " + reason);

        /**
         * Network error
         */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExpandCollapse.expand(retryContainer);
            }
        });
    }

    @Override
    public void OnMoviesFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting movies : " + reason);
    }

    @Click(R.id.retry)
    public void retryNetworkCalls(View v){
        ExpandCollapse.collapse(retryContainer);
        mWaveHelper.cancel();
        mWaveHelper.start();
        initGlobalVars();
    }
}
