package genyus.com.whichmovie.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by genyus on 18/12/15.
 */
public class NewtworkUtils {

    public static synchronized int sendRequestGet(final String url, final ArrayList<NameValuePair> nameValuePairs) {
        String result = "";
        int code = 0;
        InputStream is = null;

        try {
            HttpClient client = new DefaultHttpClient();
            DefaultHttpClient httpclient = new DefaultHttpClient(client.getParams());

            HttpGet httpGet;
            if (null != nameValuePairs) {
                String paramsString = URLEncodedUtils.format(nameValuePairs, "UTF-8");
                httpGet = new HttpGet(url + "?" + paramsString);
            } else {
                httpGet = new HttpGet(url);
            }

            Log.d(genyus.com.whichmovie.classes.Log.TAG, "url = " + httpGet.getURI());

            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            code = response.getStatusLine().getStatusCode();
            is = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        Log.d(genyus.com.whichmovie.classes.Log.TAG, "return code = " +code);
        Log.d(genyus.com.whichmovie.classes.Log.TAG, "return json = " + result);
        return code;
    }
}
