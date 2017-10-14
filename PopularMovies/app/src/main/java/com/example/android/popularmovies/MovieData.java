package com.example.android.popularmovies;

/**
 * Created by RAHUL YADAV on 18-03-2017.
 */

public class MovieData {
    private String mMovieData;
    private String mName;
    public MovieData(String movieData,String name){
        mMovieData = movieData;
        mName = name;
    }

    public String getmMovieData() {
        return mMovieData;
    }

    public String getmName() {
        return mName;
    }
}
