package genyus.com.whichmovie.model.serializer;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

import genyus.com.whichmovie.model.Crew;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.task.listener.OnMovieCrewListener;
import genyus.com.whichmovie.ui.MovieFragment;
import genyus.com.whichmovie.utils.ObjectUtils;

/**
 * Created by genyus on 09/12/15.
 */
public class CrewSerializer {

    private final static String ARRAY_CAST = "cast";
    private final static String ARRAY_CREW = "crew";
    private final static String OBJECT_ID = "id";
    private final static String OBJECT_NAME = "name";
    private final static String OBJECT_JOB = "job";
    private final static String OBJECT_PROFILE_PATH = "profile_path";
    private final static String OBJECT_CAST_ID = "cast_id";
    private final static String OBJECT_CHARACTER = "character";
    private final static String OBJECT_CREDIT_ID = "credit_id";

    public static void fillCategoriesObject(String json, int movieid, OnMovieCrewListener callback) {

        StringReader reader = new StringReader(json);
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true);

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(jsonReader);
        JsonArray ja = jo.getAsJsonArray(ARRAY_CAST);
        JsonArray crewArray = jo.getAsJsonArray(ARRAY_CREW);

        Movie movie = ObjectUtils.getMovieById(movieid);
        if(null != movie){
            if (null != ja) {
                for (JsonElement obj : ja) {
                    Crew crew = new Crew();
                    JsonObject castObject = obj.getAsJsonObject();

                    JsonElement id = castObject.get(OBJECT_ID);
                    if(!id.isJsonNull() && null != id){
                        crew.setId(id.getAsInt());
                    }

                    JsonElement name = castObject.get(OBJECT_NAME);
                    if(null != name && !name.isJsonNull() && null != name.getAsString() && !name.getAsString().isEmpty()){
                        crew.setName(name.getAsString());
                    }
                    JsonElement profile_path = castObject.get(OBJECT_PROFILE_PATH);
                    if(null!= profile_path && !profile_path.isJsonNull() && null != profile_path && null != profile_path.getAsString() && !profile_path.getAsString().isEmpty()){
                        crew.setProfile_path(profile_path.getAsString());
                    }

                    JsonElement character = castObject.get(OBJECT_CHARACTER);
                    if(null != character && !character.isJsonNull() && null != character && null != character.getAsString() && !character.getAsString().isEmpty()){
                        crew.setCharacter(character.getAsString());
                    }

                    if (null != crewArray) {
                        for (JsonElement crewMember : crewArray) {
                            JsonObject crewObject = crewMember.getAsJsonObject();
                            JsonElement job = crewObject.get(OBJECT_JOB);
                            JsonElement nameDirector = crewObject.get(OBJECT_NAME);
                            if(!job.isJsonNull() && null != job && null != job.getAsString() && !job.getAsString().isEmpty() && job.getAsString().equals("Director")){
                                if(null != nameDirector && !nameDirector.isJsonNull()&& null != nameDirector.getAsString() && !nameDirector.getAsString().isEmpty()){
                                    movie.setDirector(nameDirector.getAsString());
                                }
                            }
                        }
                    }

                    if(!movie.getCrew().contains(crew)){
                        movie.getCrew().add(crew);
                    }
                }
            }
            if(null != callback){
                if(callback instanceof MovieFragment){
                    if(((MovieFragment)callback).isAdded()){
                        callback.OnMovieCrewGet();
                    }
                } else {
                    callback.OnMovieCrewGet();
                }
                Log.w(genyus.com.whichmovie.classes.Log.TAG, "crew size = " + movie.getCrew().size());
            }
        }
    }
}
