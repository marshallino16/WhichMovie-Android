package genyus.com.whichmovie.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.greenfrvr.hashtagview.HashtagView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import genyus.com.whichmovie.MainActivity;
import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Genre;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.task.listener.OnMovieInfoListener;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.utils.PicassoTrustAll;
import genyus.com.whichmovie.utils.UnitsUtils;

/**
 * Created by genyus on 29/11/15.
 */
public class MovieFragment extends Fragment implements ObservableScrollViewCallbacks, OnMovieInfoListener {

    private Activity activity;

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
    private HashtagView hashtags;

    private LinearLayout header;
    private FrameLayout posterBlurContainer;
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
            new Thread() {
                public void run() {
                    RequestManager.getInstance(MovieFragment.this.activity).getMovieInfos(MovieFragment.this.activity, MovieFragment.this, movie.getId());
                }
            }.start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie, container, false);

        margin = view.findViewById(R.id.margin);
        overlay = view.findViewById(R.id.overlay);
        poster = (ImageView) view.findViewById(R.id.poster);
        posterBlur = (ImageView) view.findViewById(R.id.posterBlur);
        vote = (TextView) view.findViewById(R.id.vote);
        title = (TextView) view.findViewById(R.id.title);
        hashtags = (HashtagView) view.findViewById(R.id.hashtags);
        synopsis = (TextView) view.findViewById(R.id.synopsis);
        posterBlurContainer = (FrameLayout) view.findViewById(R.id.posterBlurContainer);
        ratingBarContainer = (RelativeLayout) view.findViewById(R.id.ratingBarContainer);

        header = (LinearLayout) view.findViewById(R.id.header);
        scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);

        overlay.setAlpha(0);
        posterBlurContainer.setAlpha(0);

        //header image loading
        PicassoTrustAll.getInstance(getActivity()).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getPoster_sizes().get(GlobalVars.configuration.getPoster_sizes().size() - 1) + movie.getPoster_path()).noPlaceholder().into(targetPoster);

        title.setText(""+Html.fromHtml("<bold>" + movie.getTitle() + "</bold>"));
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

        hashtags.setData(movie.getGenres(), new HashtagView.DataTransform<Genre>() {
            @Override
            public CharSequence prepare(Genre genre) {
                String label = "" + genre.getName();
                return label;
            }

        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
        title.setTranslationY(ScrollUtils.getFloat(-scrollY / 3.8f, minOverlayTransitionYTitle, 0));

        poster.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0) / 9);
        posterBlur.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0) / 9);

        overlay.setAlpha(ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        title.setAlpha(1 - ScrollUtils.getFloat((float) scrollY/ flexibleRange, 0, 1));
        posterBlurContainer.setAlpha(ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        ratingBarContainer.setAlpha(1 - ScrollUtils.getFloat((float) scrollY * 2.4f / flexibleRange, 0, 1));
        hashtags.setAlpha(1 - ScrollUtils.getFloat((float) scrollY * 2.4f / flexibleRange, 0, 1));
        ((MainActivity) getActivity()).categories.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionYTitle, 0));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    Target targetPoster = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            poster.setImageBitmap(bitmap);
            posterBlur.setImageBitmap(blurBitmap(bitmap));

            posterBlur.setScaleX(1.2f);
            posterBlur.setScaleY(1.2f);
            poster.setScaleX(1.2f);
            poster.setScaleY(1.2f);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public Bitmap blurBitmap(Bitmap bitmap) {

        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(this.activity);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        blurScript.setRadius(25.f);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        allOut.copyTo(outBitmap);
        //bitmap.recycle();
        rs.destroy();
        return outBitmap;
    }

    @Override
    public void OnMovieInfosGet() {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                title.setText(Html.fromHtml("<bold>" + movie.getTitle() + "</bold><small> - "+movie.getRuntime()+" min</small>"));
            }
        });
    }

    @Override
    public void OnMovieInfosFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting movie info : " + reason);
    }
}
