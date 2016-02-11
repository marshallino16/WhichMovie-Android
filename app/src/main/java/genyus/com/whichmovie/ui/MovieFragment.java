package genyus.com.whichmovie.ui;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.greenfrvr.hashtagview.HashtagView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import genyus.com.whichmovie.MainActivity;
import genyus.com.whichmovie.PlayerActivity_;
import genyus.com.whichmovie.R;
import genyus.com.whichmovie.WebviewActivity_;
import genyus.com.whichmovie.adapter.CrewRecyclerViewAdapter;
import genyus.com.whichmovie.adapter.ImageAdapter;
import genyus.com.whichmovie.adapter.VideoAdapter;
import genyus.com.whichmovie.classes.Quality;
import genyus.com.whichmovie.listener.OnMoviePassed;
import genyus.com.whichmovie.model.Crew;
import genyus.com.whichmovie.model.Genre;
import genyus.com.whichmovie.model.Image;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.model.Video;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.task.listener.OnMovieCrewListener;
import genyus.com.whichmovie.task.listener.OnMovieImageListener;
import genyus.com.whichmovie.task.listener.OnMovieInfoListener;
import genyus.com.whichmovie.task.listener.OnMoviePurchaseListener;
import genyus.com.whichmovie.task.listener.OnMovieVideoListener;
import genyus.com.whichmovie.task.manager.RequestManager;
import genyus.com.whichmovie.utils.AnalyticsEventUtils;
import genyus.com.whichmovie.utils.IntentUtils;
import genyus.com.whichmovie.utils.PicassoTrustAll;
import genyus.com.whichmovie.utils.ThemeUtils;
import genyus.com.whichmovie.utils.UnitsUtils;
import genyus.com.whichmovie.utils.YouTubeThumbnail;
import genyus.com.whichmovie.view.CurrencyTextView;
import genyus.com.whichmovie.view.ExpandableHeightGridView;
import genyus.com.whichmovie.view.ForegroundImageView;

/**
 * Created by genyus on 29/11/15.
 */
public class MovieFragment extends Fragment implements ObservableScrollViewCallbacks, OnMovieInfoListener, OnMovieCrewListener, OnMovieImageListener, OnMovieVideoListener, OnMoviePurchaseListener {

    private Activity activity;

    public Movie movie;
    private View view;

    public int vibrantRGB = -1;
    private float height = 0;

    //adview
    private AdView adView;
    private AdView adView2;

    //Views
    private FloatingActionButton next;
    private View margin, overlay, progressAlpha, progress;
    private TextView title, vote, synopsis, productionCompanies, releaseDate, homepage, director;
    private CurrencyTextView budget, revenue;
    private ImageView poster, posterBlur;
    private ForegroundImageView firstVideoImage, googlePlay, vudu;
    private HashtagView hashtags;
    private RecyclerView listCast;
    private ExpandableHeightGridView listImages, listVideos;

    private LinearLayout header, videoContainer;
    private FrameLayout posterBlurContainer;
    public FrameLayout fragmentContainer;
    private ObservableScrollView scrollView;
    private RelativeLayout ratingBarContainer, firstVideoControl;

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
                    RequestManager.getInstance(MovieFragment.this.activity).getMovieCrew(MovieFragment.this, movie.getId());
                    RequestManager.getInstance(MovieFragment.this.activity).getMovieImages(MovieFragment.this, movie.getId());
                    RequestManager.getInstance(MovieFragment.this.activity).getMovieVideos(MovieFragment.this, movie.getId());
                    RequestManager.getInstance(MovieFragment.this.activity).getMoviePurchase(MovieFragment.this.activity, MovieFragment.this, movie.getId(), movie);
                }
            }.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adView != null && adView2 != null) {
            adView.pause();
            adView2.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null && adView2 != null) {
            adView.resume();
            adView2.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PicassoTrustAll.getInstance(activity).cancelRequest(targetPoster);
        if (adView != null && adView2 != null) {
            adView.destroy();
            adView2.destroy();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie, container, false);

        adView = (AdView) view.findViewById(R.id.adView);
        adView2 = (AdView) view.findViewById(R.id.adView2);
        next = (FloatingActionButton) view.findViewById(R.id.fab);
        margin = view.findViewById(R.id.margin);
        overlay = view.findViewById(R.id.overlay);
        budget = (CurrencyTextView) view.findViewById(R.id.budget);
        revenue = (CurrencyTextView) view.findViewById(R.id.revenue);
        poster = (ImageView) view.findViewById(R.id.poster);
        posterBlur = (ImageView) view.findViewById(R.id.posterBlur);
        vote = (TextView) view.findViewById(R.id.vote);
        title = (TextView) view.findViewById(R.id.title);
        releaseDate = (TextView) view.findViewById(R.id.release_date);
        productionCompanies = (TextView) view.findViewById(R.id.productionCompanies);
        hashtags = (HashtagView) view.findViewById(R.id.hashtags);
        synopsis = (TextView) view.findViewById(R.id.synopsis);
        homepage = (TextView) view.findViewById(R.id.homepage);
        director = (TextView) view.findViewById(R.id.director);
        listCast = (RecyclerView) view.findViewById(R.id.cast);
        fragmentContainer = (FrameLayout) view.findViewById(R.id.fragment_root);
        firstVideoImage = (ForegroundImageView) view.findViewById(R.id.first_video_thumbnail);
        googlePlay = (ForegroundImageView) view.findViewById(R.id.googleplay);
        vudu = (ForegroundImageView) view.findViewById(R.id.vudu);
        firstVideoControl = (RelativeLayout) view.findViewById(R.id.first_video_control);
        videoContainer = (LinearLayout) view.findViewById(R.id.video_container);
        listImages = (ExpandableHeightGridView) view.findViewById(R.id.images);
        listVideos = (ExpandableHeightGridView) view.findViewById(R.id.videos);
        posterBlurContainer = (FrameLayout) view.findViewById(R.id.posterBlurContainer);
        ratingBarContainer = (RelativeLayout) view.findViewById(R.id.ratingBarContainer);

        header = (LinearLayout) view.findViewById(R.id.header);
        scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        scrollView.requestFocus();
        poster.setTag(targetPoster);

        overlay.setAlpha(0);
        posterBlurContainer.setAlpha(0);

        //ads
        AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView2.loadAd(adRequest2);

        //header image loading
        if (null != GlobalVars.configuration) {
            PicassoTrustAll.getInstance(getActivity()).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getPoster_sizes().get(GlobalVars.configuration.getPoster_sizes().size() - 2) + movie.getPoster_path()).noPlaceholder().into(targetPoster);
        }

        if (null != movie.getTitle()) {
            title.setText("" + Html.fromHtml("<b>" + movie.getTitle() + "</b>"));
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnalyticsEventUtils.sendClickAction("Title");
                }
            });
        }

        if (null != movie.getOverview()) {
            synopsis.setText("" + movie.getOverview());
            synopsis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnalyticsEventUtils.sendClickAction("Synopsis");
                }
            });
        }

        releaseDate.setText(getResources().getString(R.string.released) + " " + movie.getRelease_date());
        releaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsEventUtils.sendClickAction("Release_date");
            }
        });

        //Website homepage
        if (null != movie.getHomepage() && !movie.getHomepage().isEmpty()) {
            homepage.setVisibility(View.VISIBLE);
            homepage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnalyticsEventUtils.sendClickAction("Homepage");
                    WebviewActivity_.intent(getActivity()).movieName(movie.getTitle()).link(movie.getHomepage()).start();
                }
            });
        }

        //first video
        firstVideoControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsEventUtils.sendClickAction("Youtube_video");
                PlayerActivity_.intent(getActivity()).videoKey(String.valueOf(firstVideoImage.getTag())).start();
            }
        });

        //scroll settingup
        height = UnitsUtils.getScreenPercentHeightSize(getActivity(), 83f);
        margin.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(height)));
        //scrollView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));
        scrollView.setScrollViewCallbacks(this);
        ScrollUtils.addOnGlobalLayoutListener(scrollView, new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        //rating
        progressAlpha = new View(getActivity(), null);
        progress = new View(getActivity(), null);

        progressAlpha.setBackgroundResource(R.drawable.round_progress_alpha);
        progress.setBackgroundResource(R.drawable.round_progress);
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsEventUtils.sendClickAction("Progress");
            }
        });

        float halfWidth = UnitsUtils.getScreenPercentWidthSize(getActivity(), 50.0f);
        float progressWidth = halfWidth * ((movie.getVote_average()) * 10.0f / 100f);

        RelativeLayout.LayoutParams lpAlpha = new RelativeLayout.LayoutParams(Math.round(halfWidth), ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams lpProgress = new RelativeLayout.LayoutParams(Math.round(progressWidth), ViewGroup.LayoutParams.MATCH_PARENT);

        ratingBarContainer.addView(progress, 0, lpProgress);
        ratingBarContainer.addView(progressAlpha, 0, lpAlpha);

        vote.setText(Html.fromHtml("<strong>" + movie.getVote_average() + "</strong><small>/10</small>"));

        //tags
        hashtags.setData(movie.getGenres(), new HashtagView.DataTransform<Genre>() {
            @Override
            public CharSequence prepare(Genre genre) {
                String label = "" + genre.getName();
                return label;
            }

        });
        hashtags.addOnTagClickListener(new HashtagView.TagsClickListener() {
            @Override
            public void onItemClicked(Object item) {
                AnalyticsEventUtils.sendClickAction("Hashtags");
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "Tag click = " + item.toString());
            }
        });

        //cast
        listCast.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listCast.setLayoutManager(layoutManager);

        //next
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsEventUtils.sendClickAction("Next");
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "Next clicked");
                if (activity instanceof MainActivity) {
                    ((OnMoviePassed) activity).OnMoviePassed(MovieFragment.this);
                }
            }
        });

        //streaming
        googlePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != activity) {
                    if (null != movie.getGooglePlay()) {
                        AnalyticsEventUtils.sendStreamAction("GOOGLE_PLAY_" + movie.getGooglePlay());
                        IntentUtils.searchOnGooglePlay(activity, movie.getGooglePlay());
                    } else {
                        AnalyticsEventUtils.sendStreamAction("GOOGLE_PLAY_" + movie.getTitle());
                        IntentUtils.searchMovieOnGooglePlayByTitle(activity, movie.getTitle());
                    }
                }
            }
        });

        vudu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != activity && null != movie.getVudu()) {
                    AnalyticsEventUtils.sendStreamAction("VUDU_" + movie.getVudu());
                    IntentUtils.searchOnVudu(activity, movie.getVudu());
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //((BitmapDrawable)poster.getDrawable()).getBitmap().recycle();
        //((BitmapDrawable)posterBlur.getDrawable()).getBitmap().recycle();
        ratingBarContainer.removeAllViews();
        view = null;
        System.gc();
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
        title.setAlpha(1 - ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
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
            Bitmap blured = blurBitmap(bitmap);
            if (null != blured) {
                posterBlur.setImageBitmap(blured);
            }

            posterBlur.setScaleX(1.2f);
            posterBlur.setScaleY(1.2f);
            poster.setScaleX(1.2f);
            poster.setScaleY(1.2f);

            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();

                    if (null != vibrant && null != vibrantDark) {
                        tintAllViews(vibrant, vibrantDark);
                    }
                }
            });
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public Bitmap blurBitmap(Bitmap bitmap) {
        if(null != activity){
            try {
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
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void OnMovieInfosGet() {
        if (null != this && null != activity && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //infos
                    if (null != movie.getTitle()) {
                        title.setText(Html.fromHtml("<b>" + movie.getTitle() + "</b><small> - " + movie.getRuntime() + " min</small>"));
                    }

                    if (0 != movie.getBudget()) {
                        budget.setText("" + movie.getBudget());
                    } else {
                        budget.setText(R.string.unknown);
                    }

                    if (0 != movie.getRevenue()) {
                        revenue.setText("" + movie.getRevenue());
                    } else {
                        revenue.setText(R.string.unknown);
                    }
                    //production
                    CharSequence companies = null;
                    for (int i = 0; i < movie.getProductionCompanies().size(); ++i) {
                        if (null != companies) {
                            if (i == movie.getProductionCompanies().size() - 1) {
                                companies = android.text.TextUtils.concat(companies, Html.fromHtml(" & " + "<i><u>" + movie.getProductionCompanies().get(i) + "</u></i>"));
                            } else {
                                companies = android.text.TextUtils.concat(companies, Html.fromHtml(", " + "<i><u>" + movie.getProductionCompanies().get(i) + "</u></i> "));
                            }
                        } else {
                            companies = Html.fromHtml(getResources().getString(R.string.producted_by) + " " + "<i><u>" + movie.getProductionCompanies().get(i) + "</u></i> ");
                        }
                    }
                    for (int i = 0; i < movie.getProductionCompanies().size(); ++i) {

                    }
                    productionCompanies.setText(companies);
                }
            });
        }
    }

    @Override
    public void OnMovieInfosFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting movie info : " + reason);
    }

    @Override
    public void OnMovieCrewGet() {
        if (null != this && null != activity && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //crew
                    if (null != movie.getDirector() && !movie.getDirector().isEmpty()) {
                        director.setText(activity.getResources().getString(R.string.director, movie.getDirector()));
                    }

                    //cast
                    ArrayList<Crew> listCrew = new ArrayList<>();
                    if(null != movie.getCrew()){
                        listCrew = movie.getCrew();
                        if (listCrew.size() > 21) {
                            listCrew = new ArrayList<>(movie.getCrew().subList(0, 20));
                        }
                    }
                    final CrewRecyclerViewAdapter castAdapter = new CrewRecyclerViewAdapter(activity, listCrew);
                    castAdapter.setOnItemClickListener(new CrewRecyclerViewAdapter.OnCrewItemClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            AnalyticsEventUtils.sendClickAction("Crew");
                            if (movie.getCrew().get(position).isClicked) {
                                movie.getCrew().get(position).isClicked = false;
                            } else {
                                movie.getCrew().get(position).isClicked = true;
                            }
                            castAdapter.notifyDataSetChanged();
                        }
                    });
                    listCast.setAdapter(castAdapter);
                    listCast.scrollToPosition(0);
                }
            });
        }
    }

    @Override
    public void OnMovieCrewFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting movie crew : " + reason);
    }

    @Override
    public void OnMovieImageGet() {
        if (null != this && null != activity && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //images
                    ArrayList<Image> listImage = new ArrayList<>();
                    if(null != movie.getImages()){
                        listImage = movie.getImages();
                        if (listImage.size() > 10) {
                            listImage = new ArrayList<>(movie.getImages().subList(0, 9));
                        }
                    }
                    Log.d(genyus.com.whichmovie.classes.Log.TAG, "movie image get");
                    ImageAdapter imageAdapter = new ImageAdapter(activity, listImage, MovieFragment.this, R.id.fragment_root);
                    listImages.setNumColumns(2);
                    listImages.setAdapter(imageAdapter);
                    listImages.setExpanded(true);
                }
            });
        }
    }

    @Override
    public void OnMovieImageFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting images : " + reason);
    }

    @Override
    public void OnMovieVideoGet() {
        if (null != this && null != activity && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != getActivity()) {
                        //videos
                        if (0 == movie.getVideos().size()) {
                            videoContainer.setVisibility(View.GONE);
                        } else {
                            final String firstVideoKey = movie.getVideos().get(0).getKey();
                            PicassoTrustAll.getInstance(activity).load(YouTubeThumbnail.getUrlFromVideoId(firstVideoKey, Quality.MAXIMUM)).placeholder(android.R.color.transparent).into(firstVideoImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //nothing
                                }

                                @Override
                                public void onError() {
                                    PicassoTrustAll.getInstance(activity).load(YouTubeThumbnail.getUrlFromVideoId(firstVideoKey, Quality.HIGH)).placeholder(android.R.color.transparent).into(firstVideoImage);
                                }
                            });
                            firstVideoImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PlayerActivity_.intent(activity).videoKey(firstVideoKey).start();
                                }
                            });
                            firstVideoImage.setTag(firstVideoKey);

                            if (null != movie.getVideos() && 1 < movie.getVideos().size()) {
                                ArrayList<Video> listVideo = movie.getVideos();
                                if (listVideo.size() >= 5) {
                                    listVideo = new ArrayList<>(movie.getVideos().subList(1, 5));
                                } else {
                                    listVideo = new ArrayList<>(movie.getVideos().subList(1, movie.getVideos().size() - 1));
                                }

                                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movie videos get");
                                VideoAdapter videoAdapter = new VideoAdapter(activity, listVideo);
                                listVideos.setNumColumns(2);
                                listVideos.setAdapter(videoAdapter);
                                listVideos.setExpanded(true);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void OnMovieVideoFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting video : " + reason);
    }

    @Override
    public void OnMoviePurchase() {
        Log.d(genyus.com.whichmovie.classes.Log.TAG, "vudu = " + movie.getVudu());
        Log.d(genyus.com.whichmovie.classes.Log.TAG, "google play = " + movie.getGooglePlay());
        if (null != this && null != activity && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != activity) {
                        if (null != movie.getVudu()) {
                            if (null != view) {
                                if (null == movie.getVudu()) {
                                    view.findViewById(R.id.vudu).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void OnMoviePurchaseFailed(String reason) {
        Log.e(genyus.com.whichmovie.classes.Log.TAG, "Error getting purchase : " + reason);
    }

    private void tintAllViews(Palette.Swatch vibrant, Palette.Swatch vibrantDark) {
        if (null != view) {
            vibrantRGB = vibrant.getRgb();
            next.setBackgroundTintList(ColorStateList.valueOf(vibrant.getRgb()));

            Drawable backgroundProgress = progress.getBackground();
            Drawable backgroundProgressAlpha = progressAlpha.getBackground();
            Drawable backgroundBudget = view.findViewById(R.id.budget_indicator).getBackground();
            Drawable backgroundRevenue = view.findViewById(R.id.revenue_indicator).getBackground();

            if (backgroundProgress instanceof ShapeDrawable) {
                // cast to 'ShapeDrawable'
                ShapeDrawable shapeDrawable = (ShapeDrawable) backgroundProgress;
                shapeDrawable.getPaint().setColor(vibrant.getRgb());
            } else if (backgroundProgress instanceof GradientDrawable) {
                // cast to 'GradientDrawable'
                GradientDrawable gradientDrawable = (GradientDrawable) backgroundProgress;
                gradientDrawable.setColor(vibrant.getRgb());
            }

            if (backgroundRevenue instanceof ShapeDrawable) {
                // cast to 'ShapeDrawable'
                ShapeDrawable shapeDrawable = (ShapeDrawable) backgroundRevenue;
                shapeDrawable.getPaint().setColor(vibrant.getRgb());
            } else if (backgroundRevenue instanceof GradientDrawable) {
                // cast to 'GradientDrawable'
                GradientDrawable gradientDrawable = (GradientDrawable) backgroundRevenue;
                gradientDrawable.setColor(vibrant.getRgb());
            }

            if (backgroundBudget instanceof ShapeDrawable) {
                // cast to 'ShapeDrawable'
                ShapeDrawable shapeDrawable = (ShapeDrawable) backgroundBudget;
                shapeDrawable.getPaint().setColor(ThemeUtils.adjustAlpha(vibrantDark.getRgb(), 70f));
            } else if (backgroundBudget instanceof GradientDrawable) {
                // cast to 'GradientDrawable'
                GradientDrawable gradientDrawable = (GradientDrawable) backgroundBudget;
                gradientDrawable.setColor(ThemeUtils.adjustAlpha(vibrantDark.getRgb(), 70f));
            }

            if (backgroundProgressAlpha instanceof ShapeDrawable) {
                // cast to 'ShapeDrawable'
                ShapeDrawable shapeDrawable = (ShapeDrawable) backgroundProgressAlpha;
                shapeDrawable.getPaint().setColor(ThemeUtils.adjustAlpha(vibrantDark.getRgb(), 70f));
            } else if (backgroundProgressAlpha instanceof GradientDrawable) {
                // cast to 'GradientDrawable'
                GradientDrawable gradientDrawable = (GradientDrawable) backgroundProgressAlpha;
                gradientDrawable.setColor(ThemeUtils.adjustAlpha(vibrantDark.getRgb(), 70f));
            }

            ((TextView) view.findViewById(R.id.title1)).setTextColor(vibrant.getRgb());
            ((TextView) view.findViewById(R.id.title2)).setTextColor(vibrant.getRgb());
            ((TextView) view.findViewById(R.id.title3)).setTextColor(vibrant.getRgb());
            ((TextView) view.findViewById(R.id.title4)).setTextColor(vibrant.getRgb());
            ((TextView) view.findViewById(R.id.title5)).setTextColor(vibrant.getRgb());
            view.findViewById(R.id.line1).setBackgroundColor(vibrant.getRgb());
            view.findViewById(R.id.line2).setBackgroundColor(vibrant.getRgb());
            view.findViewById(R.id.line3).setBackgroundColor(vibrant.getRgb());
            view.findViewById(R.id.line4).setBackgroundColor(vibrant.getRgb());
            view.findViewById(R.id.line5).setBackgroundColor(vibrant.getRgb());


        /*ThemeUtils.revealColorAnimateViewDrawable(getActivity(), vibrant.getRgb(), backgroundProgress);
        ThemeUtils.revealColorAnimateViewDrawable(getActivity(), vibrant.getRgb(), backgroundRevenue);
        ThemeUtils.revealColorAnimateViewDrawableAlpha(getActivity(), vibrantDark.getRgb(), backgroundBudget);
        ThemeUtils.revealColorAnimateViewDrawableAlpha(getActivity(), vibrantDark.getRgb(), backgroundProgressAlpha);

        ThemeUtils.revealColorAnimateViewTextColor(getActivity(), vibrant.getRgb(), ((TextView)view.findViewById(R.id.title1)));
        ThemeUtils.revealColorAnimateViewTextColor(getActivity(), vibrant.getRgb(), ((TextView)view.findViewById(R.id.title2)));
        ThemeUtils.revealColorAnimateViewTextColor(getActivity(), vibrant.getRgb(), ((TextView)view.findViewById(R.id.title3)));
        ThemeUtils.revealColorAnimateViewTextColor(getActivity(), vibrant.getRgb(), ((TextView)view.findViewById(R.id.title4)));
        ThemeUtils.revealColorAnimateViewTextColor(getActivity(), vibrant.getRgb(), ((TextView)view.findViewById(R.id.title5)));

        ThemeUtils.revealColorAnimateViewBackgroundColor(getActivity(), vibrant.getRgb(), view.findViewById(R.id.line1));
        ThemeUtils.revealColorAnimateViewBackgroundColor(getActivity(), vibrant.getRgb(), view.findViewById(R.id.line2));
        ThemeUtils.revealColorAnimateViewBackgroundColor(getActivity(), vibrant.getRgb(), view.findViewById(R.id.line3));
        ThemeUtils.revealColorAnimateViewBackgroundColor(getActivity(), vibrant.getRgb(), view.findViewById(R.id.line4));
        ThemeUtils.revealColorAnimateViewBackgroundColor(getActivity(), vibrant.getRgb(), view.findViewById(R.id.line5));*/
        }
    }
}
