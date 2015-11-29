package genyus.com.whichmovie.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Movie;

/**
 * Created by genyus on 29/11/15.
 */
public class MovieFragment extends Fragment {

    public Movie movie;
    private View view;

    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragmentFirst = new MovieFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle && bundle.containsKey("movie")) {
            movie = (Movie) bundle.getSerializable("movie");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie, container, false);

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}
