package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Crew;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.utils.PicassoTrustAll;

public class CrewRecyclerViewAdapter extends RecyclerView.Adapter<CrewRecyclerViewAdapter.DataObjectHolder> {

    private Context context;
    private ArrayList<Crew> listCrew;
    private static OnCrewItemClickListener onCrewItemClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profile;
        TextView name;
        TextView nameCharac;
        RelativeLayout overlay;

        public DataObjectHolder(View itemView) {
            super(itemView);
            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.cast_name);
            nameCharac = (TextView) itemView.findViewById(R.id.cast_name_charac);
            overlay = (RelativeLayout) itemView.findViewById(R.id.overlay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(null != onCrewItemClickListener){
                onCrewItemClickListener.onItemClick(getPosition(), v);
            }
        }
    }

    public void setOnItemClickListener(OnCrewItemClickListener onCrewItemClickListener) {
        this.onCrewItemClickListener = onCrewItemClickListener;
    }

    public CrewRecyclerViewAdapter(Context context, ArrayList<Crew> myDataset) {
        this.listCrew = myDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crew_presentation, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Crew crew = listCrew.get(position);
        PicassoTrustAll.getInstance(context).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getProfile_sizes().get(1) + crew.getProfile_path()).placeholder(R.drawable.heisenberg).into(holder.profile);

        if(null == crew.getName() || crew.getName().isEmpty()){
            holder.name.setVisibility(View.INVISIBLE);
        } else {
            holder.name.setText(""+crew.getName());
        }

        if(null == crew.getCharacter() || crew.getCharacter().isEmpty()){
            holder.nameCharac.setVisibility(View.INVISIBLE);
        } else {
            holder.nameCharac.setText(""+crew.getCharacter());
        }

        if(crew.isClicked){
            holder.overlay.setVisibility(View.VISIBLE);
        }
    }

    public void addItem(Crew dataObj, int index) {
        listCrew.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        listCrew.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return listCrew.size();
    }

    public interface OnCrewItemClickListener {
        public void onItemClick(int position, View v);
    }
}