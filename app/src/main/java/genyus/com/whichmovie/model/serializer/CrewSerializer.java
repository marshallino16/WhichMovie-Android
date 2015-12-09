package genyus.com.whichmovie.model.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import genyus.com.whichmovie.model.Genre;

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

    public static void fillCategoriesObject(String json) {

        ArrayList<Genre> genres = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(json);
        JsonArray ja = jo.getAsJsonArray(ARRAY_CAST);

        if (null != ja) {
            for (JsonElement obj : ja) {

            }
        }
    }
}
