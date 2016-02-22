package genyus.com.whichmovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import genyus.com.whichmovie.adapter.MovieAdapter;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.task.listener.OnMovieQueryListener;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.utils.AnalyticsEventUtils;
import genyus.com.whichmovie.utils.PicassoTrustAll;
import genyus.com.whichmovie.utils.PreferencesUtils;
import genyus.com.whichmovie.view.ClearableAutoCompleteTextView;

/**
 * Created by GENyUS on 04/02/16.
 */
@EActivity(R.layout.activity_favorite_movie)
public class FavoriteMovieActivity extends AppCompatActivity implements OnMovieQueryListener {

    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    private int id;
    private String date;
    private String category;

    private boolean enable = false;

    @Extra
    Intent appLaunchIntent;

    @ViewById(R.id.search_autocomplete)
    ClearableAutoCompleteTextView autoCompleteTextView;

    @ViewById(R.id.validate)
    Button validate;

    @ViewById(R.id.overlay)
    View overlay;

    @ViewById(R.id.movie_thumbnail)
    ImageView movieThumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar_Parent);
        super.onCreate(savedInstanceState);
        AnalyticsEventUtils.sendScreenEnterAction(FavoriteMovieActivity.class.getName());
    }

    @AfterViews
    protected void afterView() {
        movieAdapter = new MovieAdapter(this, movies);
        autoCompleteTextView.setAdapter(movieAdapter);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString().length() >= 3) {
                    launchQueryString(s.toString());
                } else {
                    enable = false;
                }
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                hideKeyboard();
                autoCompleteTextView.setText(movies.get(position).getTitle() + "(" + movies.get(position).getRelease_date().substring(0, 4) + ")");

                if (null != movies.get(position).getPoster_path()) {
                    if (null != GlobalVars.configuration) {
                        PicassoTrustAll.getInstance(FavoriteMovieActivity.this).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getPoster_sizes().get(GlobalVars.configuration.getPoster_sizes().size() - 2) + movies.get(position).getPoster_path()).noPlaceholder().into(movieThumbnail);
                        overlay.setVisibility(View.VISIBLE);
                    }
                }

                id = movies.get(position).getId();
                date = movies.get(position).getDate();
                if(movies.get(position).getGenres().size() >= 1){
                    category = movies.get(position).getGenres().get(0).getName();
                }

                enable = true;
                AnalyticsEventUtils.sendSuggestionAction("Movie_"+movies.get(position).getTitle());
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enable) {
                    //Set first run
                    PreferencesUtils.setFirstRun(FavoriteMovieActivity.this);
                    //Store preference movie infos
                    PreferencesUtils.setStringPreference(FavoriteMovieActivity.this, PreferencesUtils.KEY_FAVORITE_DATE, date);
                    PreferencesUtils.setStringPreference(FavoriteMovieActivity.this, PreferencesUtils.KEY_FAVORITE_ID, String.valueOf(id));
                    if(null!=category){
                        PreferencesUtils.setStringPreference(FavoriteMovieActivity.this, PreferencesUtils.KEY_FAVORITE_CATEGORY, category);
                    }

                    goToNextActivity();
                } else {
                    new MaterialDialog.Builder(FavoriteMovieActivity.this)
                            .content(getResources().getString(R.string.select_movie_first))
                            .positiveText(R.string.understood)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnalyticsEventUtils.sendScreenQuitAction(FavoriteMovieActivity.class.getName());
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

    @Override
    public void OnMovieQuery(final ArrayList<Movie> listMovies) {
        Log.d(genyus.com.whichmovie.classes.Log.TAG, "listMovies size = " + listMovies.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                movies.clear();
                movies.addAll(listMovies);
                movieAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void OnMovieQueryFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting query movies : " + reason);
    }

    private void launchQueryString(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestManager.getInstance(FavoriteMovieActivity.this).getQueryMovieInfos(FavoriteMovieActivity.this, s);
            }
        }).start();
    }

    private void hideKeyboard(){
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
