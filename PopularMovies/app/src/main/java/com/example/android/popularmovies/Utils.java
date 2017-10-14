package com.example.android.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.QueryUtils.LOG_TAG;

/**
 * Created by RAHUL YADAV on 17-03-2017.
 */

public final class Utils {

    public static final String  TAG = "Utils.java";
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG,"Error in creating url",e);
            e.printStackTrace();
        }
        return url;
    }
    public static  List<MovieData> fetchMovieData(String requestUrl){
        List<MovieData> movies;
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(requestUrl.contains("reviews")){
             movies = extractReviewData(jsonResponse);
        }else{
            movies = extractMovieData(jsonResponse);
        }
        return movies;

    }
    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(50000);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Response Code"+responseCode);
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem in retreiving",e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return  output.toString();
    }
    private static List<MovieData> extractMovieData(String movieJson){
        if(TextUtils.isEmpty(movieJson)){
            return null;
        }
        List<MovieData> moviesData = new ArrayList<MovieData>();
        try{
            JSONObject root = new JSONObject(movieJson);
            JSONArray results = root.getJSONArray("results");
            for(int i=0;i<=results.length();i++){
                JSONObject movieData = results.getJSONObject(i);
                String key = movieData.getString("key");
                String name = movieData.getString("name");
                moviesData.add(new MovieData(key,name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesData;
    }
    private static List<MovieData> extractReviewData(String movieJson){
        if(TextUtils.isEmpty(movieJson)){
            return null;
        }
        List<MovieData> reviewData = new ArrayList<MovieData>();
        try{
            JSONObject root = new JSONObject(movieJson);
            JSONArray results = root.getJSONArray("results");
            for(int i=0;i<=results.length();i++){
                JSONObject movieData = results.getJSONObject(i);
                String author = movieData.getString("author");
                String content = movieData.getString("content");
                reviewData.add(new MovieData(author,content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewData;
    }



}
