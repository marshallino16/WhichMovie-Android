package genyus.com.whichmovie.task.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import genyus.com.whichmovie.utils.NetworkUtils;
import genyus.com.whichmovie.utils.UserEmailFetcher;

/**
 * Created by GENyUS on 31/01/16.
 */
public class RegistrationManager {

    public final static void registerUser(final Context mContext){
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getPackageName(), mContext.MODE_PRIVATE);

        if (prefs.getBoolean("register", true)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

                    String email = UserEmailFetcher.getEmail(mContext);
                    Log.d(genyus.com.whichmovie.classes.Log.TAG, "mail = " + email);
                    if(null != email){
                        nameValuePairs.add(new BasicNameValuePair("mail", email));
                        NetworkUtils.sendRequestGet("http://anouncement.anthony-fernandez.me/request_register_movie.php", nameValuePairs);
                    }
                }
            }).start();
            prefs.edit().putBoolean("register", false).commit();
        }
    }
}
