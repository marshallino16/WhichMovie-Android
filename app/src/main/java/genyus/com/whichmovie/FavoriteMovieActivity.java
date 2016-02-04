package genyus.com.whichmovie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import genyus.com.whichmovie.adapter.MovieSuggestionAdapter;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.task.listener.OnMovieQueryListener;
import genyus.com.whichmovie.task.manager.RequestManager;

/**
 * Created by GENyUS on 04/02/16.
 */
@EActivity(R.layout.activity_favorite_movie)
public class FavoriteMovieActivity extends AppCompatActivity implements OnMovieQueryListener {

    private MovieSuggestionAdapter suggestionAdapter;

    @Extra
    Intent appLaunchIntent;

    @ViewById(R.id.search_view)
    SearchView searchView;

    @ViewById(R.id.validate)
    Button validate;

    @ViewById(R.id.movie_thumbnail)
    ImageView movieThumbnail;

    @AfterViews
    protected void afterView() {
        String[] columns = new String[] { "_id", "title", "date", "poster"};
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        startManagingCursor(matrixCursor);
        suggestionAdapter = new MovieSuggestionAdapter(this, matrixCursor);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(1000);
        searchView.setSuggestionsAdapter(suggestionAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                launchQueryString(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                launchQueryString(s);
                return false;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                return false;
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
            public void run() {
                String[] columns = new String[] { "_id", "title", "date", "poster"};
                MatrixCursor matrixCursor = new MatrixCursor(columns);
                startManagingCursor(matrixCursor);

                for(int i=0 ; i < listMovies.size() ; ++i){
                    Movie movie = listMovies.get(i);
                    if(null != movie.getTitle() && null != movie.getPoster_path() && null != movie.getRelease_date()){
                        try {
                            matrixCursor.addRow(new Object[] {movie.getId(), new String(movie.getTitle().getBytes("ISO-8859-1")), movie.getRelease_date(), movie.getPoster_path()});
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            matrixCursor.addRow(new Object[] {movie.getId(), movie.getTitle(), movie.getRelease_date(), movie.getPoster_path()});
                        }
                    }
                }
                suggestionAdapter.changeCursor(matrixCursor);
            }
        });
    }

    @Override
    public void OnMovieQueryFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting query movies : " + reason);
    }
}
