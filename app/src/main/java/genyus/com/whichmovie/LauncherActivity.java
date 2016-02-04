package genyus.com.whichmovie;

import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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
    }
}
