package genyus.com.whichmovie.session;

import java.util.ArrayList;

import genyus.com.whichmovie.model.Configuration;
import genyus.com.whichmovie.model.Genres;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.ui.MovieFragment;

/**
 * Created by genyus on 29/11/15.
 */
public class GlobalVars {

    public static int page = 0;

    public static Configuration configuration;
    public static ArrayList<Genres> genres = new ArrayList<>();
    public static ArrayList<Movie> movies = new ArrayList<>();
    public static ArrayList<MovieFragment> fragments = new ArrayList<>();

    public static void reinitForCategoryChange(){
        movies.clear();
        fragments.clear();
        page = 0;
    }

    public static int getPage(){
        int pageToreturn = page;
        page += 1;
        return pageToreturn;
    }
}
