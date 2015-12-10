package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.model.Crew;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.utils.PicassoTrustAll;

public class CastAdapter extends BaseFlipAdapter<Crew> {

    private final int PAGES = 3;
    private Movie movie;
    private Context context;
    private LayoutInflater inflater;
    private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3};

    public CastAdapter(Context context, List<Crew> items, FlipSettings settings, Movie movie) {
        super(context, items, settings);
        this.movie = movie;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getPage(int position, View convertView, ViewGroup parent, Crew cast1, Crew cast2) {
        final FriendsHolder holder;

        if (convertView == null) {
            holder = new FriendsHolder();
            convertView = inflater.inflate(R.layout.cast_presentation, parent, false);
            holder.leftAvatar = (ImageView) convertView.findViewById(R.id.first);
            holder.rightAvatar = (ImageView) convertView.findViewById(R.id.second);
            holder.infoPage = inflater.inflate(R.layout.cast_details, parent, false);
            holder.nickName = (TextView) holder.infoPage.findViewById(R.id.nickname);

            for (int id : IDS_INTEREST)
                holder.interests.add((TextView) holder.infoPage.findViewById(id));

            convertView.setTag(holder);
        } else {
            holder = (FriendsHolder) convertView.getTag();
        }

        switch (position) {
            case 1:
                PicassoTrustAll.getInstance(context).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getProfile_sizes().get(GlobalVars.configuration.getProfile_sizes().size() - 2) + cast1.getProfile_path()).noPlaceholder().into(holder.leftAvatar);
                if (cast2 != null)
                    PicassoTrustAll.getInstance(context).load(GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getProfile_sizes().get(GlobalVars.configuration.getProfile_sizes().size() - 2) + cast2.getProfile_path()).noPlaceholder().into(holder.rightAvatar);
                break;
            default:
                fillHolder(holder, position == 0 ? cast1 : cast2);
                holder.infoPage.setTag(holder);
                return holder.infoPage;
        }
        return convertView;
    }

    @Override
    public int getPagesCount() {
        return PAGES;
    }

    private void fillHolder(FriendsHolder holder, Crew cast) {
        if (cast == null)
            return;
        Iterator<TextView> iViews = holder.interests.iterator();
        /*Iterator<String> iInterests = friend.getInterests().iterator();
        while (iViews.hasNext() && iInterests.hasNext())
            iViews.next().setText(iInterests.next());
        holder.infoPage.setBackgroundColor(getResources().getColor(friend.getBackground()));*/
        holder.nickName.setText(cast.getName());
    }

    class FriendsHolder {
        ImageView leftAvatar;
        ImageView rightAvatar;
        View infoPage;

        List<TextView> interests = new ArrayList<>();
        TextView nickName;
    }
}