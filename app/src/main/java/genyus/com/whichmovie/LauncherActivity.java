package genyus.com.whichmovie;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;

import genyus.com.whichmovie.task.manager.RequestManager;

/**
 * Created by GENyUS on 04/02/16.
 */
@EActivity(R.layout.activity_first_screen)
public class LauncherActivity extends AppCompatActivity {

    @ViewById(R.id.relevant_icon)
    ImageView relevantIcon;

    @ViewById(R.id.designed_icon)
    ImageView designedIcon;

    @ViewById(R.id.legal_icon)
    ImageView legalIcon;

    @ViewById(R.id.relevant)
    TextView relevant;

    @ViewById(R.id.designed)
    TextView designed;

    @ViewById(R.id.legal_links)
    TextView legal;

    @ViewById(R.id.next)
    Button next;

    @AfterViews
    protected void afterView() {
        getAllConfigurations();

        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);

        relevantIcon.startAnimation(slideFromLeft);
        designed.startAnimation(slideFromLeft);
        legalIcon.startAnimation(slideFromLeft);

        relevant.startAnimation(slideFromRight);
        designedIcon.startAnimation(slideFromRight);
        legal.startAnimation(slideFromRight);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteMovieActivity_.intent(LauncherActivity.this).start();
            }
        });
    }

    private void getAllConfigurations(){
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                RequestManager.getInstance(LauncherActivity.this).getConfigurations(null);
                RequestManager.getInstance(LauncherActivity.this).getAllCategories(null);
                RequestManager.getInstance(LauncherActivity.this).getMoviesFromCategory(LauncherActivity.this, null);
            }
        });
    }
}
