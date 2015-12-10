package genyus.com.whichmovie.model.serializer;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import genyus.com.whichmovie.model.Crew;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.task.listener.OnMovieCrewListener;
import genyus.com.whichmovie.utils.ObjectUtils;

/**
 * Created by genyus on 09/12/15.
 */
public class CrewSerializer {

    private final static String ARRAY_CAST = "cast";
    private final static String OBJECT_ID = "id";
    private final static String OBJECT_NAME = "name";
    private final static String OBJECT_PROFILE_PATH = "profile_path";
    private final static String OBJECT_CAST_ID = "cast_id";
    private final static String OBJECT_CHARACTER = "character";
    private final static String OBJECT_CREDIT_ID = "credit_id";

    public static void fillCategoriesObject(String json, int movieid, OnMovieCrewListener callback) {

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(json);
        JsonArray ja = jo.getAsJsonArray(ARRAY_CAST);

        Movie movie = ObjectUtils.getMovieById(movieid);
        if(null != movie){
            if (null != ja) {
                for (JsonElement obj : ja) {
                    Crew crew = new Crew();
                    JsonObject castObject = obj.getAsJsonObject();

                    JsonElement name = castObject.get(OBJECT_NAME);
                    if(!name.isJsonNull() && null != name && null != name.getAsString() && !name.getAsString().isEmpty()){
                        crew.setName(name.getAsString());
                    }
                    JsonElement profile_path = castObject.get(OBJECT_PROFILE_PATH);
                    if(!profile_path.isJsonNull() && null != profile_path && null != profile_path.getAsString() && !profile_path.getAsString().isEmpty()){
                        crew.setProfile_path(profile_path.getAsString());
                    }

                    movie.getCrew().add(crew);
                }
            }
            callback.OnMovieCrewGet();
            Log.w(genyus.com.whichmovie.classes.Log.TAG, "crew size = " + movie.getCrew().size());
        }
    }
}
