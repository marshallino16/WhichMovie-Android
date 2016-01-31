package genyus.com.whichmovie.adapter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import genyus.com.whichmovie.MainActivity;
import genyus.com.whichmovie.PhotoViewerFragment_;
import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Image;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.utils.PicassoTrustAll;

public class ImageAdapter extends ArrayAdapter<Image> {
    private Context context;
    private final ArrayList<Image> listImages;

    public ImageAdapter(Context context, ArrayList<Image> listImages) {
        super(context, 0, listImages);
        this.context = context;
        this.listImages = listImages;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_image_movie, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        Image imageObject = listImages.get(position);
        PicassoTrustAll.getInstance(context).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getBackdrop_sizes().get(1) + imageObject.getPath()).placeholder(android.R.color.transparent).into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "click image");
                FragmentTransaction transaction = ((MainActivity)context).getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, PhotoViewerFragment_.builder().positionImage(position).listImagesSlide(listImages).build());
                transaction.commit();
            }
        });

        return convertView;
    }

    @Override public int getViewTypeCount() {
        return 2;
    }

    @Override public int getItemViewType(int position) {
        return position % 2 == 0 ? 1 : 0;
    }

    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public Image getItem(int position) {
        return listImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}