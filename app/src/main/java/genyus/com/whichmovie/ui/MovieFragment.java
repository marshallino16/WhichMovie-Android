package genyus.com.whichmovie.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import genyus.com.whichmovie.MainActivity;
import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.utils.GaussianBlur;
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
    private TextView synopsis;
    private ImageView poster;
    private ImageView posterBlur;

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

        margin = view.findViewById(R.id.margin);
        overlay =  view.findViewById(R.id.overlay);
        poster = (ImageView) view.findViewById(R.id.poster);
        posterBlur = (ImageView) view.findViewById(R.id.posterBlur);
        vote = (TextView) view.findViewById(R.id.vote);
        title = (TextView) view.findViewById(R.id.title);
        synopsis = (TextView) view.findViewById(R.id.synopsis);
        ratingBarContainer = (RelativeLayout) view.findViewById(R.id.ratingBarContainer);

        header = (LinearLayout) view.findViewById(R.id.header);
        scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);

        overlay.setAlpha(0);
        posterBlur.setImageAlpha(0);

        //header image loading
        PicassoTrustAll.getInstance(getActivity()).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getPoster_sizes().get(GlobalVars.configuration.getPoster_sizes().size() - 1) + movie.getPoster_path()).noPlaceholder().into(targetPoster);

        title.setText("" + movie.getTitle());
        synopsis.setText("" + movie.getOverview());

        //scroll settingup
        height = UnitsUtils.getScreenPercentHeightSize(getActivity(), 83f);
        margin.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(height)));
        scrollView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));
        scrollView.setScrollViewCallbacks(this);

        //rating
        View progressAlpha = new View(getActivity(), null);
        View progress = new View(getActivity(), null);

        progressAlpha.setBackgroundResource(R.drawable.round_progress_alpha);
        progress.setBackgroundResource(R.drawable.round_progress);

        float halfWidth = UnitsUtils.getScreenPercentWidthSize(getActivity(), 50.0f);
        float progressWidth = halfWidth * ((movie.getVote_average()) * 10.0f / 100f);

        RelativeLayout.LayoutParams lpAlpha = new RelativeLayout.LayoutParams(Math.round(halfWidth), ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams lpProgress = new RelativeLayout.LayoutParams(Math.round(progressWidth), ViewGroup.LayoutParams.MATCH_PARENT);

        ratingBarContainer.addView(progress, 0, lpProgress);
        ratingBarContainer.addView(progressAlpha, 0, lpAlpha);

        vote.setText(Html.fromHtml("<strong>" + movie.getVote_average() + "</strong><small>/10</small>"));

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int minOverlayTransitionY = -Math.round(height);
        int minOverlayTransitionYTitle = -Math.round(height);
        float flexibleRange = height - UnitsUtils.actionBarSize(getActivity());

        header.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
        title.setTranslationY(ScrollUtils.getFloat(-scrollY / 4, minOverlayTransitionYTitle, 0));

        /*Matrix matrix = new Matrix();
        matrix.postScale(0.9f , 0.9f , 50f , 50f );
        poster.setImageMatrix(matrix);*/

        overlay.setAlpha(ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        title.setAlpha(1 - ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        posterBlur.setImageAlpha(Math.round(ScrollUtils.getFloat((float) scrollY / flexibleRange, 1, 0)));
        ratingBarContainer.setAlpha(1 - ScrollUtils.getFloat((float) scrollY * 2 / flexibleRange, 0, 1));
        ((MainActivity) getActivity()).categories.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionYTitle, 0));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    Target targetPoster = new Target(){

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            poster.setImageBitmap(bitmap);

            GaussianBlur gaussian = new GaussianBlur(getActivity());
            gaussian.setRadius(25); //max

            Bitmap output = gaussian.render(bitmap,true);
            posterBlur.setImageBitmap(output);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}
