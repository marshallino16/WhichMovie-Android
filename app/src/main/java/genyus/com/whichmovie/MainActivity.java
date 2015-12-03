package genyus.com.whichmovie;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import genyus.com.whichmovie.adapter.MoviePagerAdapter;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.ui.MovieFragment;
import genyus.com.whichmovie.view.SwipeViewPager;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private ArrayList<MovieFragment> moviesFragments = new ArrayList<>();
    private MoviePagerAdapter movieAdapter;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.moviePager)
    SwipeViewPager swipePager;

    @AfterViews
    protected void afterViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        if(BuildConfig.DEBUG){
            Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies list size = " + GlobalVars.movies.size());
        }
        for (Movie movie : GlobalVars.movies) {
            MovieFragment movieFragment = MovieFragment.newInstance(movie);
            moviesFragments.add(movieFragment);
        }

        movieAdapter = new MoviePagerAdapter(this.getSupportFragmentManager(), moviesFragments, this);
        swipePager.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
