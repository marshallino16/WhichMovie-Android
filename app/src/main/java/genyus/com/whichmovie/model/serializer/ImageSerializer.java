package genyus.com.whichmovie.model.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import genyus.com.whichmovie.model.Image;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.task.listener.OnMovieImageListener;
import genyus.com.whichmovie.utils.ObjectUtils;

/**
 * Created by genyus on 13/12/15.
 */
public class ImageSerializer {

    private final static String ARRAY_BACKDROPS = "backdrops";
    private final static String OBJECT_ID = "id";
    private final static String OBJECT_PATH = "file_path";

    public static void fillImagesObject(String json, OnMovieImageListener callback) {

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(json);
        JsonArray ja = jo.getAsJsonArray(ARRAY_BACKDROPS);

        if (null != ja) {
            for (JsonElement obj : ja) {

                JsonElement id = jo.get(OBJECT_ID);
                if(!id.isJsonNull() && null != id){
                    Movie movie = ObjectUtils.getMovieById(id.getAsInt());

                    if(null != movie){
                        JsonObject imageObject = obj.getAsJsonObject();
                        Image image = new Image();

                        JsonElement path = imageObject.get(OBJECT_PATH);

                        if(!path.isJsonNull() && null != path && null != path.getAsString() && !path.getAsString().isEmpty()){
                            image.setPath(path.getAsString());
                        }

                        if(!movie.getImages().contains(image)) {
                            movie.getImages().add(image);
                        }
                    }
                }
            }

            if(null != callback){
                callback.OnMovieImageGet();
            }
        }
    }
}
