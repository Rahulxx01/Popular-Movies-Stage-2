package com.example.android.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by RAHUL YADAV on 18-03-2017.
 */

public class MovieDataLoader extends AsyncTaskLoader<List<MovieData>> {
    String mUrl;
    public MovieDataLoader(Context context , String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<MovieData> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        List<MovieData> movieData = Utils.fetchMovieData(mUrl);
        Log.v(TAG,"url data"+mUrl);
        return movieData;
    }
}
