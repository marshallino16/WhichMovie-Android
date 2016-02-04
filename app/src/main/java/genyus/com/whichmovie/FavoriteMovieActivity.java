package genyus.com.whichmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;

/**
 * Created by GENyUS on 04/02/16.
 */
@EActivity(R.layout.activity_favorite_movie)
public class FavoriteMovieActivity extends AppCompatActivity {

    @Extra
    Intent appLaunchIntent;

    @AfterViews
    protected void afterView() {
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void goToNextActivity() {
        MainActivity_.intent(FavoriteMovieActivity.this).appLaunchIntent(getIntent()).start();
        FavoriteMovieActivity.this.finish();
    }

}
