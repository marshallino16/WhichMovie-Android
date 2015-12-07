package genyus.com.whichmovie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.Random;

import genyus.com.whichmovie.MainActivity;
import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.utils.PicassoTrustAll;
import genyus.com.whichmovie.utils.UnitsUtils;

/**
 * Created by genyus on 29/11/15.
 */
public class MovieFragment extends Fragment implements ObservableScrollViewCallbacks {

    private Movie movie;
    private View view;

    private float height = 0;

    //Views
    private View margin;
    private TextView title;
    private TextView vote;
    private ImageView backdrop;

    private LinearLayout header;
    private ObservableScrollView scrollView;

    /**
     * @param movie
     * @return
     */
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

        margin = (View) view.findViewById(R.id.margin);
        vote = (TextView) view.findViewById(R.id.vote);
        title = (TextView) view.findViewById(R.id.title);
        backdrop = (ImageView) view.findViewById(R.id.backdrop);
        RoundCornerProgressBar ratingBar = (RoundCornerProgressBar) view.findViewById(R.id.ratingBar);

        header = (LinearLayout) view.findViewById(R.id.header);
        scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);

        //header image loading
        PicassoTrustAll.getInstance(getActivity()).load(GlobalVars.configuration.getBase_url()+GlobalVars.configuration.getBackdrop_sizes().get(1)+movie.getBackdrop_path()).into(backdrop);
        title.setText(""+movie.getTitle());

        //scroll settingup
        height = UnitsUtils.getScreenPercentHeightSize(getActivity(), 33f);
        margin.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(height)));
        scrollView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));
        scrollView.setScrollViewCallbacks(this);

        //rating
        Log.d(genyus.com.whichmovie.classes.Log.TAG, "movie = "+movie.getTitle()+" vote = " + movie.getVote_average());
        ratingBar.setMax(100f);
        ratingBar.setProgress(new Random().nextFloat() * (100 - 10) + 10);
        vote.setText(""+movie.getVote_average()+"/10");

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int minOverlayTransitionY = - Math.round(height);
        header.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
        ((MainActivity)getActivity()).categories.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
