package genyus.com.whichmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by GENyUS on 04/02/16.
 */
@EActivity(R.layout.activity_first_screen)
public class LauncherActivity extends AppCompatActivity {

    @Extra
    Intent appLaunchIntent;

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
                goToNextActivity();
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void goToNextActivity() {
        FavoriteMovieActivity_.intent(LauncherActivity.this).appLaunchIntent(getIntent()).start();
        LauncherActivity.this.finish();
    }

}
