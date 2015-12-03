package genyus.com.whichmovie.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import genyus.com.whichmovie.ui.MovieFragment;

/**
 * @author genyus
 */
public class MoviePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<MovieFragment> listMovies;
    private Context context;

    public MoviePagerAdapter(FragmentManager fragmentManager, ArrayList<MovieFragment> listMovies, Context context) {
        super(fragmentManager);
        this.context = context;
        this.listMovies = listMovies;
    }

    @Override
    public Fragment getItem(int position) {
        return listMovies.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return listMovies.size();
    }
}