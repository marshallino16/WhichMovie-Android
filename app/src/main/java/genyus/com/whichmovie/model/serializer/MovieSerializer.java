package genyus.com.whichmovie.model.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.session.GlobalVars;

/**
 * Created by anthony on 11/30/15.
 */
public class MovieSerializer {

    private final static String ARRAY_RESULT = "results";
    private final static String OBJET_ADULT = "adult";
    private final static String OBJECT_BACKDROP = "backdrop_path";
    private final static String ARRAY_GENRE = "genre_ids";
    private final static String OBJECT_ID = "id";
    private final static String OBJECT_LANGAGE = "original_language";
    private final static String OBJECT_ORIGINE_TITLE = "original_title";
    private final static String OBJECT_OVERVIEW = "overview";
    private final static String OBJECT_DATE = "release_date";
    private final static String OBJECT_POSTER = "poster_path";
    private final static String OBJECT_POP = "popularity";
    private final static String OBJECT_TITLE = "title";
    private final static String OBJET_VIDEO = "video";
    private final static String OBJECT_VOTE_AVERAGE = "vote_average";
    private final static String OBJECT_VOTE_COUNT = "vote_count";

    public static void fillMoviesObject(String json) {

        ArrayList<Movie> movies = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(json);
        JsonArray ja = jo.getAsJsonArray(ARRAY_RESULT);

        if (null != ja) {
            for (JsonElement obj : ja) {

                Movie movie = new Movie();
                JsonObject movieObject = obj.getAsJsonObject();

                JsonElement id = movieObject.get(OBJECT_ID);
                JsonElement adult = movieObject.get(OBJET_ADULT);
                JsonElement backdrop = movieObject.get(OBJECT_BACKDROP);
                JsonElement genre = movieObject.get(ARRAY_GENRE);
                JsonElement langage = movieObject.get(OBJECT_LANGAGE);
                JsonElement origine_title = movieObject.get(OBJECT_ORIGINE_TITLE);
                JsonElement overview = movieObject.get(OBJECT_OVERVIEW);
                JsonElement date = movieObject.get(OBJECT_DATE);
                JsonElement poster = movieObject.get(OBJECT_POSTER);
                JsonElement popularity = movieObject.get(OBJECT_POP);
                JsonElement title = movieObject.get(OBJECT_TITLE);
                JsonElement video = movieObject.get(OBJET_VIDEO);
                JsonElement vote_average = movieObject.get(OBJECT_VOTE_AVERAGE);
                JsonElement vote_count = movieObject.get(OBJECT_VOTE_COUNT);

                if(!id.isJsonNull() && null != id){
                    movie.setId(id.getAsInt());
                }

                movies.add(movie);
            }
        }

        GlobalVars.movies = movies;
    }
}
