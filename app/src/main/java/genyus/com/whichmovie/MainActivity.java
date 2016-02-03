package genyus.com.whichmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;

import genyus.com.whichmovie.adapter.CategoryAdapter;
import genyus.com.whichmovie.adapter.MoviePagerAdapter;
import genyus.com.whichmovie.listener.OnMoviePassed;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.task.listener.OnMoviesListener;
import genyus.com.whichmovie.task.listener.OnNewMoviesListener;
import genyus.com.whichmovie.task.manager.RegistrationManager;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.ui.MovieFragment;
import genyus.com.whichmovie.utils.AppUtils;
import genyus.com.whichmovie.utils.ObjectUtils;
import genyus.com.whichmovie.utils.PreferencesUtils;
import genyus.com.whichmovie.view.SwipeViewPager;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements OnMoviesListener, OnMoviePassed, OnNewMoviesListener, AdapterView.OnItemSelectedListener {

    //deeplinking
    Branch branch;

    private ArrayList<MovieFragment> moviesFragments = new ArrayList<>();
    private ArrayList<Movie> movies = new ArrayList<>();
    private MoviePagerAdapter movieAdapter;
    private CategoryAdapter categoryAdapter;

    @Extra
    Intent appLaunchIntent;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.moviePager)
    public SwipeViewPager swipePager;

    @ViewById(R.id.categories)
    public Spinner categories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread() {
            public void run() {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "get movies");
                RequestManager.getInstance(MainActivity.this).getMoviesFromCategory(MainActivity.this, MainActivity.this);
            }
        }.start();
    }

    @AfterViews
    protected void afterViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        if (BuildConfig.DEBUG) {
            Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies list size = " + GlobalVars.movies.size());
        }

        generateFragmentFromMovies();

        movieAdapter = new MoviePagerAdapter(this.getSupportFragmentManager(), new ArrayList<MovieFragment>(), this);
        swipePager.setAdapter(movieAdapter);
        swipePager.setOffscreenPageLimit(0);
        swipePager.setPagingEnabled(false);
        swipePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "swipepager size = " + swipePager.getChildCount());
                if (position == swipePager.getChildCount() - 1) {
                    Log.d(genyus.com.whichmovie.classes.Log.TAG, "should request new movies");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(genyus.com.whichmovie.classes.Log.TAG, "request new movies");
                            RequestManager.getInstance(MainActivity.this).getNewMoviesFromCategory(MainActivity.this, MainActivity.this);
                        }
                    }).start();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        categoryAdapter = new CategoryAdapter(this, R.layout.row_spinner_categories, GlobalVars.genres);
        categories.setAdapter(categoryAdapter);
        categories.setSelection(ObjectUtils.getGenrePositionById(PreferencesUtils.getDefaultCategory(this)));
        categories.post(new Runnable() {
            public void run() {
                categories.setOnItemSelectedListener(MainActivity.this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        branch = Branch.getInstance(this.getApplicationContext());
        Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {

            }
        };
        branch.initSession(branchReferralInitListener, this.getIntent().getData(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //REGISTRATION
        RegistrationManager.registerUser(this);
        //Version code
        PreferencesUtils.setLastAppVersionCode(this);
        //Check available update
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isAnUpdateAvailable = AppUtils.isAnUpdateAvailable(MainActivity.this);
            }
        }).start();
        //Notification
        AppUtils.configureNotification(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != branch){
            branch.closeSession();
        }
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
            PreferencesActivity_.intent(this).start();
            return true;
        } else if (id == R.id.action_share){
            String title =  moviesFragments.get(swipePager.getCurrentItem()).movie.getTitle();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_message, title));

            Log.d(genyus.com.whichmovie.classes.Log.TAG, "title = " + title);
            startActivity(Intent.createChooser(share, getString(R.string.share_title)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @Override
    public void OnNewMoviesGet() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Movie> moviesTemp = new ArrayList<>();
                for(int j=0 ; j < GlobalVars.movies.size() ; ++j){
                    if(!movies.contains(GlobalVars.movies.get(j))){
                        movies.add(GlobalVars.movies.get(j));
                        moviesTemp.add(GlobalVars.movies.get(j));
                    }
                }
                for (Movie movie : moviesTemp) {
                    MovieFragment movieFragment = MovieFragment.newInstance(movie);
                    if(!moviesFragments.contains(movieFragment)){
                        moviesFragments.add(movieFragment);
                    } else {
                        Log.d(genyus.com.whichmovie.classes.Log.TAG, "fragment contained");
                    }
                }
                movieAdapter.setData(moviesFragments);
                swipePager.invalidate();
            }
        });
    }

    @Override
    public void OnNewMoviesFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting new movies : " + reason);
    }

    @Override
    public void OnMoviePassed(MovieFragment fragment) {
        if(null != swipePager){
            Log.e(genyus.com.whichmovie.classes.Log.TAG, "Change viewpager position.");
            if(null != moviesFragments){
                if(1 < moviesFragments.size()){
                    moviesFragments.remove(fragment);
                    movieAdapter.notifyDataSetChanged();
                } else {
                    //TODO
                    //search more movies
                }
            }
        }
    }

    private void generateFragmentFromMovies(){
        movies.clear();
        moviesFragments.clear();
        movies.addAll(GlobalVars.movies);
        for (Movie movie : GlobalVars.movies) {
            MovieFragment movieFragment = MovieFragment.newInstance(movie);
            moviesFragments.add(movieFragment);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
        new Thread() {
            public void run() {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "on item selected");
                GlobalVars.reinitForCategoryChange(MainActivity.this);
                PreferencesUtils.setPreference(MainActivity.this, PreferencesUtils.KEY_DEFAULT_CATEGORY, GlobalVars.genres.get(i).getId());
                RequestManager.getInstance(MainActivity.this).getMoviesFromCategory(MainActivity.this, MainActivity.this);
            }
        }.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
