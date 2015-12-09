package genyus.com.whichmovie.model.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import genyus.com.whichmovie.model.Genre;
import genyus.com.whichmovie.session.GlobalVars;

/**
 * Created by genyus on 29/11/15.
 */
public class CategoriesSerializer {

    private final static String ARRAY_GENRE = "genres";
    private final static String OBJECT_ID = "id";
    private final static String OBJECT_NAME = "name";

    public static void fillCategoriesObject(String json) {

        ArrayList<Genre> genres = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(json);
        JsonArray ja = jo.getAsJsonArray(ARRAY_GENRE);

        if (null != ja) {
            for (JsonElement obj : ja) {

                Genre genre = new Genre();
                JsonObject genreObject = obj.getAsJsonObject();

                JsonElement id = genreObject.get(OBJECT_ID);
                JsonElement name = genreObject.get(OBJECT_NAME);

                if(!id.isJsonNull() && null != id){
                    genre.setId(id.getAsInt());
                }

                if(!name.isJsonNull() && null != name){
                    genre.setName(name.getAsString());
                }

                genres.add(genre);
            }
        }

        GlobalVars.genres = genres;
    }
}
