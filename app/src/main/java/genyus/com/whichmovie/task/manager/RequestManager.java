package genyus.com.whichmovie.task.manager;

import android.content.Context;

import genyus.com.whichmovie.api.APIConst;
import genyus.com.whichmovie.classes.RequestReturn;
import genyus.com.whichmovie.task.listener.OnConfigurationListener;

/**
 * Created by genyus on 28/11/15.
 */
public class RequestManager {

    private static final int ATTEMPT_MAX = 2;

    private Context context = null;
    private int currentAttempt = 0;
    private static RequestManager instance = null;

    public static synchronized RequestManager getInstance(Context context) {
        if (null == instance) {
            instance = new RequestManager(context);
        }
        return instance;
    }

    private RequestManager(Context context) {
        this.context = context;
    }

    public void getConfigurations(OnConfigurationListener callback){
        currentAttempt += 1;
        RequestReturn returnedCode = RequestSender.sendRequestGet(APIConst.API_BASE_URL, APIConst.API_CONFIGURATION, null);
        if (null != returnedCode) {
            if (200 == returnedCode.code) {
                //Log.d(TAG, "categories json = " + returnedCode.json);
                //CategorieSerializer.fillCategorieObject(returnedCode.json);
                currentAttempt = 0;
                callback.OnConfigurationGet();
                return;
            } else {
                this.getConfigurations(callback);
            }
        } else {
            if (ATTEMPT_MAX == currentAttempt) {
                currentAttempt = 0;
                callback.OnConfigurationFailed(null);
                return;
            } else {
                this.getConfigurations(callback);
            }
        }
    }

    public void getAllCategories(){

    }

    public void getMoviesFromCategory(){

    }

    public void getMovieInfos(){

    }

    public void getMovieCrew(){

    }

    public void getMovieImages(){

    }

    public void getMovieVideos(){

    }
}
