package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

import genyus.com.whichmovie.ui.MovieFragment;

/**
 * @author genyus
 */
public class MoviePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<MovieFragment> listMovies;
    private Context context;

    public MoviePagerAdapter(FragmentManager fragmentManager, ArrayList<MovieFragment> listMovies, Context context) {
        super(fragmentManager);
        this.context = context;
        this.listMovies = listMovies;
    }

    public void setData(ArrayList<MovieFragment>  data) {
        this.listMovies = data;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return listMovies.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return listMovies.size();
    }
}