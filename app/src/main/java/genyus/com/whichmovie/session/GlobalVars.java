package genyus.com.whichmovie.session;

import android.content.Context;

import java.util.ArrayList;

import genyus.com.whichmovie.model.Configuration;
import genyus.com.whichmovie.model.Genre;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.ui.MovieFragment;
import genyus.com.whichmovie.utils.PreferencesUtils;

/**
 * Created by genyus on 29/11/15.
 */
public class GlobalVars {

    public static int page = 1;

    public static Configuration configuration;
    public static ArrayList<Genre> genres = new ArrayList<>();
    public static ArrayList<Movie> movies = new ArrayList<>();
    public static ArrayList<MovieFragment> fragments = new ArrayList<>();

    public static void init(Context context){
        page = PreferencesUtils.getPagePreference(context);
    }

    public static void reinitForCategoryChange(Context context){
        movies.clear();
        fragments.clear();
        page = 1;
        PreferencesUtils.setPagePreference(context, 1);
    }

    public static int getPage(Context context){
        int pageToreturn = page;
        page += 1;
        PreferencesUtils.setPagePreference(context, page);
        return pageToreturn;
    }
}
