package genyus.com.whichmovie;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by GENyUS on 04/02/16.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends AppCompatActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

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
}
