package genyus.com.whichmovie.task.listener;

/**
 * Created by genyus on 29/11/15.
 */
public interface OnNewMoviesListener {

    public void OnNewMoviesGet();
    public void OnNewMoviesFailed(String reason);

}
