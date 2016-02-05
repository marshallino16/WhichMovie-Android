package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import genyus.com.whichmovie.PlayerActivity_;
import genyus.com.whichmovie.R;
import genyus.com.whichmovie.classes.Quality;
import genyus.com.whichmovie.model.Video;
import genyus.com.whichmovie.utils.AnalyticsEventUtils;
import genyus.com.whichmovie.utils.PicassoTrustAll;
import genyus.com.whichmovie.utils.YouTubeThumbnail;

public class VideoAdapter extends ArrayAdapter<Video> {

    private Context context;
    private final ArrayList<Video> listVideos;

    Map<View, YouTubeThumbnailLoader> loaders = new HashMap<>(

    );

    public VideoAdapter(Context context, ArrayList<Video> listVideos) {
        super(context, 0, listVideos);
        this.context = context;
        this.listVideos = listVideos;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String videoId = listVideos.get(position).getKey();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_video_movie, null);
        }

        final ImageView thumbnail = (ImageView) convertView.findViewById(R.id.youtube_thumbnail_view);
        ImageView play = (ImageView) convertView.findViewById(R.id.play);
        ViewCompat.setElevation(play, 10f);

        PicassoTrustAll.getInstance(context).load(YouTubeThumbnail.getUrlFromVideoId(listVideos.get(position).getKey(), Quality.MAXIMUM)).placeholder(android.R.color.transparent).into(thumbnail, new Callback() {
            @Override
            public void onSuccess() {
                //nothing
            }

            @Override
            public void onError() {
                PicassoTrustAll.getInstance(context).load(YouTubeThumbnail.getUrlFromVideoId(listVideos.get(position).getKey(), Quality.DEFAULT)).placeholder(android.R.color.transparent).into(thumbnail);
            }
        });
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsEventUtils.sendClickAction("Video_"+listVideos.get(position).getType()+"_"+listVideos.get(position).getKey());
                PlayerActivity_.intent(context).videoKey(listVideos.get(position).getKey()).start();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return listVideos.size();
    }

    @Override
    public Video getItem(int position) {
        return listVideos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}