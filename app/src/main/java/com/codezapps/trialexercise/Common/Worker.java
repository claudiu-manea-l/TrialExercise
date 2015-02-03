package com.codezapps.trialexercise.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viperime on 2/3/2015.
 */
public class Worker extends AsyncTask<String,Void,JSONHolder> {

    private static final String SEARCH_STRING = "searchString";
    private static final String SEARCH_TYPE = "searchType";
    private static final String SEARCH_OFFSET= "offset";

    public static final int INITIAL_REQUEST = 0;
    public static final int SHOWMORE_REQUEST = 1;
    public static final int SEARCH_REQUEST = 2;

    private IWorkerCallback mCallback;
    private int mRequestType;
    private int mPosition;
    public Worker(IWorkerCallback callback,int request)
    {
        mCallback = callback;
        mRequestType = request;
        mPosition=-1;
    }
    public Worker(IWorkerCallback callback,int request,int position){
        this(callback,request);
        mPosition=position;
    }

    protected JSONHolder doInBackground(String... param) {
        InputStream is = null;
        String result ="";
        HttpEntity entity= null;
        String urlString = "http://trials.mtcmobile.co.uk/api/football/1.0/search";

       Map<String,String> map = getPairs(param[0],param[1],param[2]);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(urlString);
        try {

            String jsonRequest = new GsonBuilder().create().toJson(map,Map.class);
            httpPost.setEntity(new StringEntity(jsonRequest));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(httpPost);
            entity = response.getEntity();
            is = entity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        JSONHolder jsonHolder = new JSONHolder();
        jsonHolder.parse(result);
        return jsonHolder;
    }

    protected void onPostExecute(JSONHolder result) {
        mCallback.postFinished(result,mRequestType,mPosition);
    }

    private Map<String,String> getPairs(String searchString,String searchType,String offset)
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put(SEARCH_STRING, searchString);
        map.put(SEARCH_TYPE, searchType);
        map.put(SEARCH_OFFSET, offset);
        return map;
    }
}
