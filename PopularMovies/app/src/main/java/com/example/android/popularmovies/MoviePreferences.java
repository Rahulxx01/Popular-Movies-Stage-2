package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import static android.R.attr.id;

/**
 * Created by RAHUL YADAV on 27-02-2017.
 */

public class MoviePreferences {

    public static String getPreferenceMovieUrlString(Context context){
        String prefMovieByUser="popular";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForMovies = context.getString(R.string.pref_movie_key);
        String prefTopRated = context.getString(R.string.pref_units_toprated);
        String prefPopular = context.getString(R.string.pref_unit_mostpopular);
        String prefFavourites = context.getString(R.string.pref_units_favourites);
        String prefDefault = context.getString(R.string.pref_unit_mostpopular);


        String pref_finalString =  sharedPreferences.getString(keyForMovies,prefDefault);
        if(pref_finalString.equals(prefPopular)){
          prefMovieByUser = prefPopular;

        }else if(pref_finalString.equals(prefTopRated)){
            prefMovieByUser = prefTopRated;

        }else if(pref_finalString.equals(prefFavourites)){
            prefMovieByUser =  prefFavourites;
        }
        return prefMovieByUser;

      //  return prefFavourite;
    }
}
