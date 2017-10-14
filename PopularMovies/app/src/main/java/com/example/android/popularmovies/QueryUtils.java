package com.example.android.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.GridLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by RAHUL YADAV on 18-02-2017.
 */

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {

    }

    //to create url//
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error in creating url", e);
            e.printStackTrace();
        }
        return url;
    }

    public static List<Movie> fetchMovieData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Movie> movies = extractMovies(jsonResponse);
        return movies;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(50000);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Response Code" + responseCode);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in retreiving", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Movie> extractMovies(String movieJson) {
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }
        List<Movie> movies = new ArrayList<Movie>();
        try {
            JSONObject root = new JSONObject(movieJson);
            JSONArray results = root.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieData = results.getJSONObject(i);
                String movieTitle = movieData.getString("original_title");
                String movieReleaseDate = movieData.getString("release_date");
                String moviePlotSynopsis = movieData.getString("overview");
                String moviePosterPath = movieData.getString("poster_path");
                Double movieVoteAverage = movieData.getDouble("vote_average");
                int movieID = movieData.getInt("id");
                movies.add(new Movie(movieID, movieTitle, movieReleaseDate, moviePlotSynopsis, moviePosterPath, movieVoteAverage));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static String storeImage(Context context, String url,
                                    final String absPath, final String movieId,
                                    final String type) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                File file = new File(absPath + "/"
                        + movieId
                        + "_" + type + ".jpg");

                try {
                    if (file.createNewFile()) {
                        FileOutputStream opStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, opStream);
                        opStream.flush();
                        opStream.close();
                    } else {
                        Log.w(LOG_TAG, "Unable to save file");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context).load(url).into(target);
        return absPath + "/" + movieId + "_" + type + ".jpg";
    }

    public static void deleteImage(String absPath, String movieId, String type) {
        File file = new File(absPath + "/"
                + movieId + "_" + type + ".jpg");

        if (file.exists() && file.delete()) {
            Log.i(LOG_TAG, "Image deleted successfully");
        }
    }


}
