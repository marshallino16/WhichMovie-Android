package genyus.com.whichmovie.model.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.model.Video;
import genyus.com.whichmovie.task.listener.OnMovieVideoListener;
import genyus.com.whichmovie.ui.MovieFragment;
import genyus.com.whichmovie.utils.ObjectUtils;

/**
 * Created by GENyUS on 30/01/16.
 */
public class VideoSerializer {

    private final static String ARRAY_RESULT = "results";
    private final static String OBJECT_ID = "id";
    private final static String OBJECT_KEY = "key";
    private final static String OBJECT_SIZE = "size";
    private final static String OBJECT_TYPE = "type";
    private final static String OBJECT_SITE = "site";
    private final static String OBJECT_NAME = "name";

    public static void fillVideosObject(String json, OnMovieVideoListener callback) {

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(json);
        JsonArray ja = jo.getAsJsonArray(ARRAY_RESULT);

        if (null != ja) {
            for (JsonElement obj : ja) {

                JsonElement id = jo.get(OBJECT_ID);
                if(!id.isJsonNull() && null != id){
                    Movie movie = ObjectUtils.getMovieById(id.getAsInt());

                    if(null != movie){
                        JsonObject videoObject = obj.getAsJsonObject();
                        Video video = new Video();

                        JsonElement key = videoObject.get(OBJECT_KEY);
                        JsonElement size = videoObject.get(OBJECT_SIZE);
                        JsonElement type = videoObject.get(OBJECT_TYPE);
                        JsonElement site = videoObject.get(OBJECT_SITE);
                        JsonElement name = videoObject.get(OBJECT_NAME);

                        if(!key.isJsonNull() && null != key && null != key.getAsString() && !key.getAsString().isEmpty()){
                            video.setKey(key.getAsString());
                        }

                        if(!site.isJsonNull() && null != site && null != site.getAsString() && !site.getAsString().isEmpty()){
                            video.setSite(site.getAsString());
                        }

                        if(!type.isJsonNull() && null != type && null != type.getAsString() && !type.getAsString().isEmpty()){
                            video.setType(type.getAsString());
                        }

                        if(!name.isJsonNull() && null != name && null != name.getAsString() && !name.getAsString().isEmpty()) {
                            video.setName(name.getAsString());
                        }

                        if(!size.isJsonNull() && null != size){
                            video.setSize(size.getAsInt());
                        }

                        if(!site.isJsonNull() && null != site && null != site.getAsString()){
                            if(!movie.getVideos().contains(video) && site.getAsString().equals(Video.SITE_YOUTUBE)) {
                                movie.getVideos().add(video);
                            }
                        }
                    }
                }
            }

            if (null != callback) {
                if(callback instanceof MovieFragment){
                    if(((MovieFragment)callback).isAdded()){
                        callback.OnMovieVideoGet();
                    }
                } else {
                    callback.OnMovieVideoGet();
                }
            }
        }
    }

}
