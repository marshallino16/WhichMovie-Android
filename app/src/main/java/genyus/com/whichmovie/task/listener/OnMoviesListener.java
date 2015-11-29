package genyus.com.whichmovie.task.listener;

/**
 * Created by genyus on 29/11/15.
 */
public interface OnMoviesListener {

    public void OnMoviesGet();
    public void OnMoviesFailed(String reason);

}
