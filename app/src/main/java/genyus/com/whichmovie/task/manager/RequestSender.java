package genyus.com.whichmovie.task.manager;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import genyus.com.whichmovie.classes.RequestReturn;


public class RequestSender {

    public static synchronized RequestReturn sendRequestPost(final String url, final String apiPoint, final ArrayList<NameValuePair> nameValuePairs) {
        String result = "";
        int code = 0;
        InputStream is = null;

        try {
            HttpClient client = new DefaultHttpClient();
            DefaultHttpClient httpclient = new DefaultHttpClient(client.getParams());

            // Set verifier
            HttpPost httpPost;
            httpPost = new HttpPost(url + apiPoint);
            //httpPost.setHeader("CONTENT_TYPE", "application/x-www-form-urlencoded; charset=utf-8");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));//, "UTF-8"

            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            code = response.getStatusLine().getStatusCode();
            is = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            return null;
        }

        return new RequestReturn(result, code);
    }


    public static synchronized RequestReturn sendRequestGet(final String url, final String apiPoint, final ArrayList<NameValuePair> nameValuePairs) {
        String result = "";
        int code = 0;
        InputStream is = null;

        try {
            HttpClient client = new DefaultHttpClient();
            DefaultHttpClient httpclient = new DefaultHttpClient(client.getParams());

            HttpGet httpGet;
            if (null != nameValuePairs) {
                String paramsString = URLEncodedUtils.format(nameValuePairs, "UTF-8");
                httpGet = new HttpGet(url + apiPoint + "?" + paramsString);
            } else {
                httpGet = new HttpGet(url + apiPoint);
            }

            Log.d(genyus.com.whichmovie.classes.Log.TAG, "url = " + httpGet.getURI());

            httpGet.setHeader("CONTENT_TYPE", "application/x-www-form-urlencoded; charset=utf-8");

            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            code = response.getStatusLine().getStatusCode();
            is = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            return null;
        }

        return new RequestReturn(result, code);
    }

    public static synchronized RequestReturn sendRequestGetWithHeader(final String url, final String apiPoint, final ArrayList<NameValuePair> nameValuePairs, final Header[] header) {
        String result = "";
        int code = 0;
        InputStream is = null;

        try {
            HttpClient client = new DefaultHttpClient();
            DefaultHttpClient httpclient = new DefaultHttpClient(client.getParams());

            HttpGet httpGet;
            if (null != nameValuePairs) {
                String paramsString = URLEncodedUtils.format(nameValuePairs, "UTF-8");
                httpGet = new HttpGet(url + apiPoint + "?" + paramsString);
            } else {
                httpGet = new HttpGet(url + apiPoint);
            }

            Log.d(genyus.com.whichmovie.classes.Log.TAG, "url = " + httpGet.getURI());

            httpGet.setHeaders(header);

            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            code = response.getStatusLine().getStatusCode();
            is = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            return null;
        }

        return new RequestReturn(result, code);
    }

    @Deprecated
    /**
     * API specify this endpoint type but, it isnt relevent for us
     */
    public synchronized RequestReturn sendRequestDelete(final String url, final String apiPoint, final ArrayList<NameValuePair> nameValuePairs) {
        return null;
    }

    @Deprecated
    /**
     * API specify this endpoint type but, it isnt relevent for us
     */
    public synchronized RequestReturn sendRequestPut(final String url, final String apiPoint, final ArrayList<NameValuePair> nameValuePairs) {
        return null;
    }
}
