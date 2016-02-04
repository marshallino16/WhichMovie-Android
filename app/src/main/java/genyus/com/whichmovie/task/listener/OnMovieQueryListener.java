package genyus.com.whichmovie.task.listener;

import java.util.ArrayList;

import genyus.com.whichmovie.model.Movie;

/**
 * Created by GENyUS on 04/02/16.
 */
public interface OnMovieQueryListener {
    public void OnMovieQuery(ArrayList<Movie> listMovies);
    public void OnMovieQueryFailed(String reason);
}
