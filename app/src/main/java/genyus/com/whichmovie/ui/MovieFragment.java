package genyus.com.whichmovie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.utils.PicassoTrustAll;

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

        ImageView backdrop = (ImageView) view.findViewById(R.id.backdrop);
        PicassoTrustAll.getInstance(getActivity()).load(GlobalVars.configuration.getBase_url()+GlobalVars.configuration.getBackdrop_sizes().get(1)+movie.getBackdrop_path()).into(backdrop);

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}
