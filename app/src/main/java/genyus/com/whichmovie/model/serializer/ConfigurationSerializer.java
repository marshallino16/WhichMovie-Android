package genyus.com.whichmovie.model.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import genyus.com.whichmovie.model.Configuration;
import genyus.com.whichmovie.session.GlobalVars;

/**
 * Created by genyus on 28/11/15.
 */
public class ConfigurationSerializer {

    private final static String OBJECT_IMAGES = "images";
    private final static String OBJECT_URL = "base_url";
    private final static String OBJECT_URL_S = "secure_base_url";

    private final static String ARRAY_BACKDROP = "backdrop_sizes";
    private final static String ARRAY_LOGO = "logo_sizes";
    private final static String ARRAY_POSTER = "poster_sizes";
    private final static String ARRAY_PROFIL = "profile_sizes";
    private final static String ARRAY_STILL = "still_sizes";
    private final static String ARRAY_KEYS = "change_keys";

    public static void fillConfigurationObject(String json){

        Configuration conf = new Configuration();

        StringReader reader = new StringReader(json);
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true);

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(jsonReader);
        JsonObject ja = jo.getAsJsonObject(OBJECT_IMAGES);

        if(null != ja){

            if(!ja.get(OBJECT_URL).isJsonNull() && null != ja.get(OBJECT_URL)){
                conf.setBase_url(ja.get(OBJECT_URL).getAsString());
            }

            if(!ja.get(OBJECT_URL_S).isJsonNull() && null != ja.get(OBJECT_URL_S)){
                conf.setSecure_base_url(ja.get(OBJECT_URL_S).getAsString());
            }

            if(!ja.get(ARRAY_BACKDROP).isJsonNull() && null != ja.get(ARRAY_BACKDROP) && null != ja.get(ARRAY_BACKDROP).getAsJsonArray()){
                Type listType = new TypeToken<List<String>>() {}.getType();
                ArrayList<String> backdrops = new Gson().fromJson(ja.get(ARRAY_BACKDROP), listType);
                conf.setBackdrop_sizes(backdrops);
            }

            if(!ja.get(ARRAY_LOGO).isJsonNull() && null != ja.get(ARRAY_LOGO) && null != ja.get(ARRAY_LOGO).getAsJsonArray()){
                Type listType = new TypeToken<List<String>>() {}.getType();
                ArrayList<String> logos = new Gson().fromJson(ja.get(ARRAY_LOGO), listType);
                conf.setLogo_sizes(logos);
            }

            if(!ja.get(ARRAY_POSTER).isJsonNull() && null != ja.get(ARRAY_POSTER) && null != ja.get(ARRAY_POSTER).getAsJsonArray()){
                Type listType = new TypeToken<List<String>>() {}.getType();
                ArrayList<String> posters = new Gson().fromJson(ja.get(ARRAY_POSTER), listType);
                conf.setPoster_sizes(posters);
            }

            if(!ja.get(ARRAY_PROFIL).isJsonNull() && null != ja.get(ARRAY_PROFIL) && null != ja.get(ARRAY_PROFIL).getAsJsonArray()){
                Type listType = new TypeToken<List<String>>() {}.getType();
                ArrayList<String> profiles = new Gson().fromJson(ja.get(ARRAY_PROFIL), listType);
                conf.setProfile_sizes(profiles);
            }

            if(!ja.get(ARRAY_STILL).isJsonNull() && null != ja.get(ARRAY_STILL) && null != ja.get(ARRAY_STILL).getAsJsonArray()){
                Type listType = new TypeToken<List<String>>() {}.getType();
                ArrayList<String> stills = new Gson().fromJson(ja.get(ARRAY_STILL), listType);
                conf.setStill_sizes(stills);
            }
        }

        GlobalVars.configuration = conf;
    }
}
