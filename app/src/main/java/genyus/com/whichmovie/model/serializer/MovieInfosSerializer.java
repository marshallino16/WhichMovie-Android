package genyus.com.whichmovie.model.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;

import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.task.listener.OnMovieInfoListener;
import genyus.com.whichmovie.ui.MovieFragment;
import genyus.com.whichmovie.utils.ObjectUtils;

/**
 * Created by anthony on 12/2/15.
 */
public class MovieInfosSerializer {

    private final static String OBJECT_ID = "id";
    private final static String OBJECT_NAME = "name";
    private final static String OBJECT_BUDGET = "budget";
    private final static String OBJECT_HOMEPAGE = "homepage";
    private final static String OBJECT_REVENUE = "revenue";
    private final static String OBJECT_RUNTIME = "runtime";
    private final static String OBJECT_IMDB_ = "imdb_id";
    private final static String ARRAY_PRODUCTION = "production_companies";

    public static void fillConfigurationObject(String json, OnMovieInfoListener callback) {

        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(json);

        if (null != jo) {

            JsonElement id = jo.get(OBJECT_ID);
            if(!id.isJsonNull() && null != id){
                Movie movie = ObjectUtils.getMovieById(id.getAsInt());

                if(null != movie){
                    JsonElement budget = jo.get(OBJECT_BUDGET);
                    JsonElement revenue = jo.get(OBJECT_REVENUE);
                    JsonElement runtime = jo.get(OBJECT_RUNTIME);
                    JsonElement imdb = jo.get(OBJECT_IMDB_);
                    JsonElement homepage = jo.get(OBJECT_HOMEPAGE);
                    JsonElement production = jo.get(ARRAY_PRODUCTION);

                    if(!imdb.isJsonNull() && null != imdb && null != imdb.getAsString() && !imdb.getAsString().isEmpty()){
                        movie.setImdb(imdb.getAsString());
                    }

                    if(!homepage.isJsonNull() && null != homepage && null != homepage.getAsString() && !homepage.getAsString().isEmpty()){
                        movie.setHomepage(homepage.getAsString());
                    }

                    if(!budget.isJsonNull() && null != budget){
                        movie.setBudget(budget.getAsInt());
                    }

                    if(!revenue.isJsonNull() && null != revenue){
                        movie.setRevenue(revenue.getAsInt());
                    }

                    if(!runtime.isJsonNull() && null != runtime){
                        movie.setRuntime(runtime.getAsInt());
                    }

                    if(!production.isJsonNull() && null != production){
                        JsonArray companies = production.getAsJsonArray();
                        if(null != companies){
                            for (JsonElement obj : companies) {
                                JsonElement name = obj.getAsJsonObject().get(OBJECT_NAME);
                                if(!name.isJsonNull() && null != name){
                                    try {
                                        movie.getProductionCompanies().add(new String(name.getAsString().getBytes("ISO-8859-1")));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                        movie.getProductionCompanies().add(name.getAsString());
                                    } catch (NullPointerException e) {
                                        movie.getProductionCompanies().add(name.getAsString());
                                    }
                                }
                            }
                        }
                    }

                }
            }

            if(null != callback){
                if(callback instanceof MovieFragment){
                    if(((MovieFragment)callback).isAdded() && ((MovieFragment)callback).isInLayout()){
                        callback.OnMovieInfosGet();
                    }
                } else {
                    callback.OnMovieInfosGet();
                }
            }
        }
    }
}