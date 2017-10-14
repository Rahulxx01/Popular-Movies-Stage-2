package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.Movie;

import static android.R.attr.version;
import static android.os.Build.VERSION_CODES.M;
import static android.os.FileObserver.CREATE;

/**
 * Created by RAHUL YADAV on 14-03-2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "favourite.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_FAVOURITE_TABLE = "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + "("
                + MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER + " BLOB, "
             /*   + MoviesContract.MovieEntry.COLUMN_MOVIE_GRID_LINK + " TEXT NOT NULL, "*/
                + MoviesContract.MovieEntry.COLUMN_MOVIE_RATING + " REAL NOT NULL, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_TRAILER + " TEXT NOT NULL, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_FAVOURITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
