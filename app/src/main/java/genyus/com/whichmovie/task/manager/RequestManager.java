package genyus.com.whichmovie.task.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import genyus.com.whichmovie.BuildConfig;
import genyus.com.whichmovie.api.APIConst;
import genyus.com.whichmovie.classes.RequestReturn;
import genyus.com.whichmovie.model.Movie;
import genyus.com.whichmovie.model.serializer.CategoriesSerializer;
import genyus.com.whichmovie.model.serializer.ConfigurationSerializer;
import genyus.com.whichmovie.model.serializer.CrewSerializer;
import genyus.com.whichmovie.model.serializer.ImageSerializer;
import genyus.com.whichmovie.model.serializer.MovieInfosSerializer;
import genyus.com.whichmovie.model.serializer.MovieSerializer;
import genyus.com.whichmovie.model.serializer.VideoSerializer;
import genyus.com.whichmovie.session.GlobalVars;
import genyus.com.whichmovie.task.listener.OnCategoriesListener;
import genyus.com.whichmovie.task.listener.OnConfigurationListener;
import genyus.com.whichmovie.task.listener.OnMovieCrewListener;
import genyus.com.whichmovie.task.listener.OnMovieImageListener;
import genyus.com.whichmovie.task.listener.OnMovieInfoListener;
import genyus.com.whichmovie.task.listener.OnMoviePurchaseListener;
import genyus.com.whichmovie.task.listener.OnMovieQueryListener;
import genyus.com.whichmovie.task.listener.OnMovieVideoListener;
import genyus.com.whichmovie.task.listener.OnMoviesListener;
import genyus.com.whichmovie.task.listener.OnNewMoviesListener;
import genyus.com.whichmovie.ui.MovieFragment;
import genyus.com.whichmovie.utils.AppUtils;
import genyus.com.whichmovie.utils.PreferencesUtils;
import genyus.com.whichmovie.utils.UnitsUtils;

/**
 * Created by genyus on 28/11/15.
 */
public class RequestManager {

    private static final int ATTEMPT_MAX = 2;

    private Context context = null;
    private int currentAttempt = 0;
    private static RequestManager instance = null;

    public static RequestManager getInstance(Context context) {
        if (null == instance) {
            instance = new RequestManager(context);
        }
        return instance;
    }

    private RequestManager(Context context) {
        this.context = context;
    }

    public void getConfigurations(OnConfigurationListener callback) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_CONFIGURATION, nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            Log.d(genyus.com.whichmovie.classes.Log.TAG, "configuration code = " + 200);
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "configuration json = " + returnedCode.json);
                ConfigurationSerializer.fillConfigurationObject(returnedCode.json);
                currentAttempt = 0;
                if (null != callback) {
                    callback.OnConfigurationGet();
                }
                return;
            } else {
                this.getConfigurations(callback);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                if (null != callback) {
                    //callback.OnConfigurationFailed(null);
                }
                return;
            } else {
                this.getConfigurations(callback);
            }
        }
    }

    public void getAllCategories(OnCategoriesListener callback) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_LIST_CATEGORIES, nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "categories json = " + returnedCode.json);
                CategoriesSerializer.fillCategoriesObject(returnedCode.json);
                currentAttempt = 0;
                if (null != callback) {
                    callback.OnCategoriesGet();
                }
                return;
            } else {
                this.getAllCategories(callback);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                if (null != callback) {
                    //callback.OnCategoriesFailed(null);
                }
                return;
            } else {
                this.getAllCategories(callback);
            }
        }
    }

    public void getMoviesFromCategory(Context context, OnMoviesListener callback) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        nameValuePairs.add(new BasicNameValuePair("with_genres", String.valueOf(PreferencesUtils.getDefaultCategory(context))));
        nameValuePairs.add(new BasicNameValuePair("include_adult", "false"));
        nameValuePairs.add(new BasicNameValuePair("page", String.valueOf(GlobalVars.getPage(context))));
        nameValuePairs.add(new BasicNameValuePair("release_date.lte", UnitsUtils.getNowTime()));
        if (AppUtils.isDeviceInFrench()) {
            nameValuePairs.add(new BasicNameValuePair("language", "fr"));
        }

        if (BuildConfig.DEBUG) {
            String date = PreferencesUtils.getStringPreference(context, PreferencesUtils.KEY_FAVORITE_DATE);
            if (null != date && date.length() > 4) {
                nameValuePairs.add(new BasicNameValuePair("year", date.substring(0, 4)));
            }
        }

        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_LIST_MOVIES_CATEGORY, nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies json = " + returnedCode.json);

                MovieSerializer.fillMoviesObject(returnedCode.json, callback);
                currentAttempt = 0;
                return;
            } else {
                this.getMoviesFromCategory(context, callback);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                if (null != callback) {
                   // callback.OnMoviesFailed(null);
                }
                return;
            } else {
                this.getMoviesFromCategory(context, callback);
            }
        }
    }

    public void getNewMoviesFromCategory(Context context, OnNewMoviesListener callback) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        nameValuePairs.add(new BasicNameValuePair("with_genres", String.valueOf(PreferencesUtils.getDefaultCategory(context))));
        nameValuePairs.add(new BasicNameValuePair("include_adult", "false"));
        nameValuePairs.add(new BasicNameValuePair("page", String.valueOf(GlobalVars.getPage(context))));

        if (BuildConfig.DEBUG) {
            String date = PreferencesUtils.getStringPreference(context, PreferencesUtils.KEY_FAVORITE_DATE);
            if (null != date && date.length() > 4) {
                nameValuePairs.add(new BasicNameValuePair("year", date.substring(0, 4)));
            }
        }

        nameValuePairs.add(new BasicNameValuePair("release_date.lte", UnitsUtils.getNowTime()));
        if (AppUtils.isDeviceInFrench()) {
            nameValuePairs.add(new BasicNameValuePair("language", "fr"));
        }

        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_LIST_MOVIES_CATEGORY, nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies json = " + returnedCode.json);

                MovieSerializer.fillNewMoviesObject(returnedCode.json, callback);
                currentAttempt = 0;
                return;
            } else {
                this.getNewMoviesFromCategory(context, callback);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                //callback.OnNewMoviesFailed(null);
                return;
            } else {
                this.getNewMoviesFromCategory(context, callback);
            }
        }
    }

    public void getMovieInfos(Context context, OnMovieInfoListener callback, int movieId) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        if (AppUtils.isDeviceInFrench()) {
            nameValuePairs.add(new BasicNameValuePair("language", "fr"));
        }
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_INFO_MOVIE(movieId), nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies info json = " + returnedCode.json);

                MovieInfosSerializer.fillConfigurationObject(returnedCode.json, callback);
                currentAttempt = 0;
                return;
            } else {
                this.getMovieInfos(context, callback, movieId);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                //callback.OnMovieInfosFailed(null);
                return;
            } else {
                this.getMovieInfos(context, callback, movieId);
            }
        }
    }

    public void getQueryMovieInfos(OnMovieQueryListener callback, String query) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        nameValuePairs.add(new BasicNameValuePair("query", query));
        if (AppUtils.isDeviceInFrench()) {
            nameValuePairs.add(new BasicNameValuePair("language", "fr"));
        }
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_SEARCH, nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies query info json = " + returnedCode.json);

                MovieSerializer.fillMinimalistMoviesObject(returnedCode.json, callback);
                currentAttempt = 0;
                return;
            } else {
                this.getQueryMovieInfos(callback, query);
            }
        } else {
            currentAttempt = 0;
            //callback.OnMovieQueryFailed(null);
            return;
        }
    }

    /**
     * Using date ranges to avoid many calls
     *
     * @param context
     * @param callback
     */
    @Deprecated
    public void getMoviePlaying(Context context, OnMoviesListener callback) {
        /*currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        nameValuePairs.add(new BasicNameValuePair("page", String.valueOf(GlobalVars.getPage())));
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_NOW_PLAYING, nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies info json = " + returnedCode.json);

                MovieSerializer.fillListMoviesObject(returnedCode.json);
                currentAttempt = 0;
                if(GlobalVars.page < GlobalVars.totalPlayingPages){
                    GlobalVars.page += 1;
                    this.getMoviePlaying(context, callback);
                } else {
                    getMoviesFromCategory(context, callback);
                }
                return;
            } else {
                this.getMoviePlaying(context, callback);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                return;
            } else {
                this.getMoviePlaying(context, callback);
            }
        }*/
    }

    public void getMovieCrew(OnMovieCrewListener callback, int movieId) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        if (AppUtils.isDeviceInFrench()) {
            nameValuePairs.add(new BasicNameValuePair("language", "fr"));
        }
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_CREW_MOVIE(movieId), nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies crew json = " + returnedCode.json);

                CrewSerializer.fillCategoriesObject(returnedCode.json, movieId, callback);
                currentAttempt = 0;
                return;
            } else {
                this.getMovieCrew(callback, movieId);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                //callback.OnMovieCrewFailed(null);
                return;
            } else {
                this.getMovieCrew(callback, movieId);
            }
        }
    }

    public void getMovieImages(OnMovieImageListener callback, int movieId) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_IMAGES_MOVIE(movieId), nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies image json = " + returnedCode.json);

                ImageSerializer.fillImagesObject(returnedCode.json, callback);
                currentAttempt = 0;
                return;
            } else {
                this.getMovieImages(callback, movieId);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                //callback.OnMovieImageFailed(null);
                return;
            } else {
                this.getMovieImages(callback, movieId);
            }
        }
    }

    public void getMovieVideos(OnMovieVideoListener callback, int movieId) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("api_key", APIConst.API_TOKEN));
        if (AppUtils.isDeviceInFrench()) {
            nameValuePairs.add(new BasicNameValuePair("language", "fr"));
        }
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_VIDEOS_MOVIE(movieId), nameValuePairs);
        if (null != returnedCode && !returnedCode.json.contains("Authentication error")) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies video json = " + returnedCode.json);

                VideoSerializer.fillVideosObject(returnedCode.json, callback);
                currentAttempt = 0;
                return;
            } else {
                this.getMovieVideos(callback, movieId);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                //callback.OnMovieVideoFailed(null);
                return;
            } else {
                this.getMovieVideos(callback, movieId);
            }
        }
    }

    public void getMoviePurchase(Context context, OnMoviePurchaseListener callback, int movieId, Movie movie) {
        currentAttempt += 1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_PURCHASE_BASE_URL(context), APIConst.API_PURCHASE_ID + String.valueOf(movieId), nameValuePairs);
        if (null != returnedCode) {
            if (200 == returnedCode.code) {
                Log.d(genyus.com.whichmovie.classes.Log.TAG, "movies purchase json = " + returnedCode.json);

                try {
                    //serializing
                    JsonParser parser = new JsonParser();
                    JsonObject jo = (JsonObject) parser.parse(returnedCode.json);

                    if (null != jo) {
                        JsonElement id = jo.get("id");
                        if (null != id && !id.isJsonNull()) {
                            int identifiant = id.getAsInt();

                            //purchase link
                            ArrayList<NameValuePair> nameValuePairsLink = new ArrayList<>();
                            RequestReturn returnedCodeLink = RequestSender.sendRequestGet(APIConst.API_PURCHASE_BASE_URL(context), APIConst.API_PURCHASE_LINK + String.valueOf(identifiant), nameValuePairsLink);
                            if (null != returnedCodeLink) {
                                if (200 == returnedCodeLink.code) {
                                    JsonParser parserLink = new JsonParser();
                                    JsonObject joLink = (JsonObject) parserLink.parse(returnedCodeLink.json);
                                    JsonArray ja = joLink.getAsJsonArray("purchase_android_sources");

                                    if (null != ja) {
                                        for (JsonElement obj : ja) {
                                            JsonObject linkObject = obj.getAsJsonObject();

                                            if (null != linkObject && !linkObject.get("source").isJsonNull() && null != linkObject.get("source").getAsString() && linkObject.get("source").getAsString().equals("vudu")) {
                                                if (!linkObject.get("link").isJsonNull() && null != linkObject.get("link").getAsString()) {
                                                    movie.setVudu(linkObject.get("link").getAsString());
                                                }
                                            }

                                            if (null != linkObject && !linkObject.get("source").isJsonNull() && null != linkObject.get("source").getAsString() && linkObject.get("source").getAsString().equals("google_play")) {
                                                if (!linkObject.get("link").isJsonNull() && null != linkObject.get("link").getAsString()) {
                                                    movie.setGooglePlay(linkObject.get("link").getAsString());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception error) {
                    error.printStackTrace();
                }

                if(null != callback){
                    if(callback instanceof MovieFragment){
                        if(((MovieFragment)callback).isAdded() && ((MovieFragment)callback).isInLayout()){
                            callback.OnMoviePurchase();
                        }
                    } else {
                        callback.OnMoviePurchase();
                    }
                }
                currentAttempt = 0;
                return;
            } else {
                this.getMoviePurchase(context, callback, movieId, movie);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                //callback.OnMoviePurchaseFailed(null);
                return;
            } else {
                this.getMoviePurchase(context, callback, movieId, movie);
            }
        }
    }
}
