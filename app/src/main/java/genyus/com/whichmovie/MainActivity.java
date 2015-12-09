package genyus.com.whichmovie;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import genyus.com.whichmovie.adapter.CategoryAdapter;
import genyus.com.whichmovie.adapter.MoviePagerAdapter;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.task.listener.OnMoviesListener;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.ui.MovieFragment;
import genyus.com.whichmovie.utils.ObjectUtils;
import genyus.com.whichmovie.utils.PreferencesUtils;
import genyus.com.whichmovie.view.SwipeViewPager;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements OnMoviesListener {

    private ArrayList<MovieFragment> moviesFragments = new ArrayList<>();
    private MoviePagerAdapter movieAdapter;
    private CategoryAdapter categoryAdapter;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.moviePager)
    SwipeViewPager swipePager;

    @ViewById(R.id.categories)
    public Spinner categories;

    @AfterViews
    protected void afterViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        if(BuildConfig.DEBUG){
            Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies list size = " + GlobalVars.movies.size());
        }

        generateFragmentFromMovies();

        movieAdapter = new MoviePagerAdapter(this.getSupportFragmentManager(), moviesFragments, this);
        swipePager.setAdapter(movieAdapter);
        swipePager.setPagingEnabled(false);

        categoryAdapter = new CategoryAdapter(this, R.layout.spinner_categories, GlobalVars.genres);
        categories.setAdapter(categoryAdapter);
        categories.setSelection(ObjectUtils.getGenrePositionById(PreferencesUtils.getDefaultCategory(this)));
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                new Thread() {
                    public void run() {
                        GlobalVars.reinitForCategoryChange();
                        PreferencesUtils.setPreference(MainActivity.this, PreferencesUtils.KEY_DEFAULT_CATEGORY, GlobalVars.genres.get(position).getId());
                        RequestManager.getInstance(MainActivity.this).getMoviesFromCategory(MainActivity.this, MainActivity.this);
                    }
                }.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        //super.onBackPressed();
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void OnMoviesGet() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                generateFragmentFromMovies();
                movieAdapter.setData(moviesFragments);
                swipePager.invalidate();
                swipePager.setCurrentItem(0);
            }
        });
    }

    @Override
    public void OnMoviesFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting movies : " + reason);
    }

    private void generateFragmentFromMovies(){
        moviesFragments.clear();
        for (Movie movie : GlobalVars.movies) {
            MovieFragment movieFragment = MovieFragment.newInstance(movie);
            moviesFragments.add(movieFragment);
        }
    }
}
