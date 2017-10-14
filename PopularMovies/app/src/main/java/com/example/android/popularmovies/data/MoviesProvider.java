package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.R.attr.defaultHeight;
import static android.R.attr.id;

/**
 * Created by RAHUL YADAV on 14-03-2017.
 */

public class MoviesProvider extends ContentProvider {
    private MoviesDbHelper mDbHelper;
    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String LOG_TAG = "MoviesProvider";
    static {
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_MOVIE,MOVIE);

        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_MOVIE+"/#",MOVIE_ID);

    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri,  String[] projection, String selection, String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE :
                //perform on entire table//
                cursor = sqLiteDatabase.query(MoviesContract.MovieEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
           case MOVIE_ID:
                //perfrom on single row of the table//
                selection = MoviesContract.MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(MoviesContract.MovieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:  throw new IllegalArgumentException("Cannot query unknowm Uri" + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Override
    public String getType( Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
              //  return MoviesContract.MovieEntry.CONTENT_URI;
                return null;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri,  ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return insertFavourites(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported" + uri);
        }

    }
    private Uri insertFavourites(Uri uri,ContentValues contentValues){
        String movieName = contentValues.getAsString(MoviesContract.MovieEntry.COLUMN_MOVIE_NAME);
        String movieID = contentValues.getAsString(MoviesContract.MovieEntry.COLUMN_MOVIE_ID);
        String moviePosterLink = contentValues.getAsString(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER);
        String movieVoteAverage = contentValues.getAsString(MoviesContract.MovieEntry.COLUMN_MOVIE_RATING);
        String moviePlotSynopsis = contentValues.getAsString(MoviesContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS);
        String movieReleaseDate = contentValues.getAsString(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        // Get writeable database
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        long newRowID = sqLiteDatabase.insert(MoviesContract.MovieEntry.TABLE_NAME, null, contentValues);
        if (newRowID == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match){
            case MOVIE:
                // Delete a single row given by the ID in the URI
               // selection = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?";

               //selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                rowsDeleted = sqLiteDatabase.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri,  ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case MOVIE:
                rowsUpdated = sqLiteDatabase.update(MoviesContract.MovieEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
