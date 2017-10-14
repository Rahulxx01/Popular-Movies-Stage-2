package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by RAHUL YADAV on 14-03-2017.
 */

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";


    public static abstract class MovieEntry implements BaseColumns{
        
        public static final String TABLE_NAME = "movie";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_NAME = "movieName";
     //   public static final String COLUMN_MOVIE_GRID_LINK = "gridImageLink";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_SYNOPSIS = "movieSynopsis";
        public static final String COLUMN_MOVIE_RATING  = "movieRating";
        public static final String COLUMN_MOVIE_TRAILER = "movieTrailer";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE);

    }

}
