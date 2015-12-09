package genyus.com.whichmovie.api;

/**
 * Created by genyus on 25/11/15.
 */
public class APIConst {

    public final static String API_TOKEN = "c1e4b32ab37e5086fe5c09521c0e67a7";
    public final static String API_BASE_URL = "http://api.themoviedb.org/3/";
    public final static String API_LIST_CATEGORIES = "genre/movie/list";
    public final static String API_CONFIGURATION = "configuration";
    public final static String API_LIST_MOVIES_CATEGORY = "discover/movie";
    public final static String API_NOW_PLAYING = "movie/now_playing";

    public final static String API_INFO_MOVIE(int movie){
        return "movie/"+movie;
    }

    public final static String API_CREW_MOVIE(int movie){
        return "movie/"+movie+"/credits";
    }

    public final static String API_VIDEOS_MOVIE(int movie){
        return "movie/"+movie+"/videos";
    }

    public final static String API_IMAGES_MOVIE(int movie){
        return "movie/"+movie+"/images";
    }
}
