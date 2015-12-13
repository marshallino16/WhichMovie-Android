package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Image;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.utils.PicassoTrustAll;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.DataObjectHolder> {

    private Context context;
    private ArrayList<Image> listImage;
    private static OnImageItemClickListener onImageItemClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profile;

        public DataObjectHolder(View itemView) {
            super(itemView);
            profile = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(null != onImageItemClickListener){
                onImageItemClickListener.onItemClick(getPosition(), v);
            }
        }
    }

    public void setOnItemClickListener(OnImageItemClickListener onImageItemClickListener) {
        this.onImageItemClickListener = onImageItemClickListener;
    }

    public ImageRecyclerViewAdapter(Context context, ArrayList<Image> myDataset) {
        this.listImage = myDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_movie, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Image image = listImage.get(position);
        PicassoTrustAll.getInstance(context).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getBackdrop_sizes().get(1) + image.getPath()).placeholder(R.drawable.heisenberg).into(holder.profile);
    }

    public void addItem(Image dataObj, int index) {
        listImage.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        listImage.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    public interface OnImageItemClickListener {
        public void onItemClick(int position, View v);
    }
}