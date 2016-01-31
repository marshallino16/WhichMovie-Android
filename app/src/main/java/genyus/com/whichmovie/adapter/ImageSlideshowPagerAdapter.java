package genyus.com.whichmovie.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Image;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.utils.PicassoTrustAll;

public class ImageSlideshowPagerAdapter extends PagerAdapter {

    private Activity context;
    private final ArrayList<Image> listImages;
    private LayoutInflater inflater;

    public ImageSlideshowPagerAdapter(Activity context, ArrayList<Image> images) {
        this.context = context;
        this.listImages = images;
    }

    @Override
    public int getCount() {
        return this.listImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.row_pager_slideshow_image, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.slideShowImage);
        PicassoTrustAll.getInstance(context).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getBackdrop_sizes().get(1) + listImages.get(position).getPath()).placeholder(android.R.color.transparent).into(imageView);

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
