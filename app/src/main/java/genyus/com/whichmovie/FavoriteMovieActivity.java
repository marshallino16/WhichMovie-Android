package genyus.com.whichmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import genyus.com.whichmovie.adapter.MovieAdapter;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.task.listener.OnMovieQueryListener;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.utils.PicassoTrustAll;
import genyus.com.whichmovie.view.ClearableAutoCompleteTextView;

/**
 * Created by GENyUS on 04/02/16.
 */
@EActivity(R.layout.activity_favorite_movie)
public class FavoriteMovieActivity extends AppCompatActivity implements OnMovieQueryListener {

    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    @Extra
    Intent appLaunchIntent;

    @ViewById(R.id.search_autocomplete)
    ClearableAutoCompleteTextView autoCompleteTextView;

    @ViewById(R.id.validate)
    Button validate;

    @ViewById(R.id.movie_thumbnail)
    ImageView movieThumbnail;

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
                if(!s.toString().isEmpty() && s.toString().length() >= 3){
                    launchQueryString(s.toString());
                } else {
                    validate.setEnabled(false);
                }
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    autoCompleteTextView.setText(new String(movies.get(position).getTitle().getBytes("ISO-8859-1"))+"("+movies.get(position).getRelease_date().substring(0,4)+")");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    autoCompleteTextView.setText(movies.get(position).getTitle()+"("+movies.get(position).getRelease_date().substring(0,4)+")");
                }

                if(null != movies.get(position).getPoster_path()){
                    if (null != GlobalVars.configuration) {
                        PicassoTrustAll.getInstance(FavoriteMovieActivity.this).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getPoster_sizes().get(GlobalVars.configuration.getPoster_sizes().size() - 2) + movies.get(position).getPoster_path()).noPlaceholder().into(movieThumbnail);
                    }
                }

                validate.setEnabled(true);
            }
        });
    }

    private void launchQueryString(final String s){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestManager.getInstance(FavoriteMovieActivity.this).getQueryMovieInfos(FavoriteMovieActivity.this, s);
            }
        }).start();
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
            public void run() {movies.clear();
                movies.addAll(listMovies);
                movieAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void OnMovieQueryFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting query movies : " + reason);
    }
}
