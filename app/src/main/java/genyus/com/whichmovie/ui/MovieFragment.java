package genyus.com.whichmovie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

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
    private View overlay;
    private TextView title;
    private TextView vote;
    private ImageView backdrop;

    private LinearLayout header;
    private ObservableScrollView scrollView;
    private RelativeLayout ratingBarContainer;

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
        overlay = (View) view.findViewById(R.id.overlay);
        vote = (TextView) view.findViewById(R.id.vote);
        title = (TextView) view.findViewById(R.id.title);
        backdrop = (ImageView) view.findViewById(R.id.backdrop);
        ratingBarContainer = (RelativeLayout) view.findViewById(R.id.ratingBarContainer);

        header = (LinearLayout) view.findViewById(R.id.header);
        scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);

        overlay.setAlpha(0);

        //header image loading
        PicassoTrustAll.getInstance(getActivity()).load(GlobalVars.configuration.getBase_url()+GlobalVars.configuration.getBackdrop_sizes().get(1)+movie.getBackdrop_path()).into(backdrop);
        title.setText(""+movie.getTitle());

        //scroll settingup
        height = UnitsUtils.getScreenPercentHeightSize(getActivity(), 33f);
        margin.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(height)));
        scrollView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));
        scrollView.setScrollViewCallbacks(this);

        //rating
        View progressAlpha = new View(getActivity(), null);
        View progress = new View(getActivity(), null);

        progressAlpha.setBackgroundResource(R.drawable.round_progress_alpha);
        progress.setBackgroundResource(R.drawable.round_progress);

        float halfWidth = UnitsUtils.getScreenPercentWidthSize(getActivity(), 50.0f);
        float progressWidth = halfWidth*((movie.getVote_average())*10.0f/100f);

        RelativeLayout.LayoutParams lpAlpha = new RelativeLayout.LayoutParams(Math.round(halfWidth), ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams lpProgress = new RelativeLayout.LayoutParams(Math.round(progressWidth), ViewGroup.LayoutParams.MATCH_PARENT);

        ratingBarContainer.addView(progress, 0, lpProgress);
        ratingBarContainer.addView(progressAlpha, 0, lpAlpha);

        vote.setText(Html.fromHtml("<strong>"+movie.getVote_average()+"</strong><small>/10</small>"));

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
        int minOverlayTransitionYTitle = - Math.round(height/2);
        float flexibleRange = height - UnitsUtils.actionBarSize(getActivity());

        header.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
        title.setTranslationY(ScrollUtils.getFloat(-scrollY / 4, minOverlayTransitionYTitle, 0));
        overlay.setAlpha(ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        ((MainActivity)getActivity()).categories.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionYTitle, 0));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
