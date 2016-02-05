package genyus.com.whichmovie;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import genyus.com.whichmovie.animation.ExpandCollapse;
import genyus.com.whichmovie.classes.MovieActivity;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.task.listener.OnCategoriesListener;
import genyus.com.whichmovie.task.listener.OnConfigurationListener;
import genyus.com.whichmovie.task.listener.OnMoviesListener;
import genyus.com.whichmovie.task.manager.RegistrationManager;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.utils.PreferencesUtils;
import genyus.com.whichmovie.utils.WaveUtils;
import genyus.com.whichmovie.view.FlakeView;
import genyus.com.whichmovie.view.WaveView;

@EActivity(R.layout.activity_loading)
public class LoadingActivity extends MovieActivity implements OnConfigurationListener, OnCategoriesListener, OnMoviesListener{

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

        GlobalVars.init(this);
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
        //REGISTRATION
        RegistrationManager.registerUser(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void goToNextActivity() {
        if(PreferencesUtils.isFirstRun(this)){
            PreferencesUtils.setFirstRun(this);
            LauncherActivity_.intent(LoadingActivity.this).appLaunchIntent(getIntent()).start();
        } else {
            MainActivity_.intent(LoadingActivity.this).appLaunchIntent(getIntent()).start();
        }
        LoadingActivity.this.finish();
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
