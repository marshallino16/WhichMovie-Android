package genyus.com.whichmovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import genyus.com.whichmovie.utils.AnalyticsEventUtils;

/**
 * Created by GENyUS on 04/02/16.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends AppCompatActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsEventUtils.sendScreenEnterAction(AboutActivity.class.getName());
    }

    @AfterViews
    protected void afterView() {
        //toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.about));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        AboutActivity.this.finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnalyticsEventUtils.sendScreenQuitAction(AboutActivity.class.getName());
    }
}
