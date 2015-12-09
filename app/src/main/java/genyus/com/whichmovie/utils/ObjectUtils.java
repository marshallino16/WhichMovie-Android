package genyus.com.whichmovie.utils;

import genyus.com.whichmovie.model.Genre;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;

/**
 * Created by anthony on 12/8/15.
 */
public class ObjectUtils {

    public static final Genre getGenreById(int id){
        Genre genre = null;
        for (Genre genres: GlobalVars.genres) {
            if(genres.getId() == id){
                genre = genres;
                break;
            }
        }
        return genre;
    }

    public static final Movie getMovieById(int id){
        Movie movie = null;
        for (Movie movies: GlobalVars.movies) {
            if(movies.getId() == id){
                movie = movies;
                break;
            }
        }
        return movie;
    }


    public static final int getGenrePositionById(int id){
        int position = 0;
        for(int i=0; i<GlobalVars.genres.size() ; ++i){
            Genre genre = GlobalVars.genres.get(i);
            if(genre.getId() == id){
                position = i;
                break;
            }
        }
        return position;
    }
}
