package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MoviesContract;

import static android.R.attr.name;
import static android.R.attr.rating;


public class FavouritesDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    TextView mFavouritesMovieTitle;
    ImageView mFavouritesMovieImage;
    TextView mFavouritesMovieRating;
    TextView mFavouritesMovieReleaseDate;
    TextView mFavouritesMovieSynopsis;
    Uri currentMovieUri;
    ListView mTrailerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFavouritesMovieTitle = (TextView) findViewById(R.id.movie_title_favourites);
        mFavouritesMovieImage = (ImageView) findViewById(R.id.movie_poster_favourites);
        mFavouritesMovieReleaseDate = (TextView) findViewById(R.id.movie_release_date_favourites);
        mFavouritesMovieSynopsis = (TextView) findViewById(R.id.movie_plot_synopsis_favourites);
        mFavouritesMovieRating = (TextView) findViewById(R.id.movie_vote_average_favourites);
        mTrailerListView = (ListView) findViewById(R.id.trailer_list_view_favourites);

        Intent intent = getIntent();
        currentMovieUri = intent.getData();
        Log.v("currentMovieuri",currentMovieUri.toString());

        getLoaderManager().initLoader(3, null, this);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String projection[] = {
                MoviesContract.MovieEntry._ID,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
                MoviesContract.MovieEntry.COLUMN_MOVIE_NAME,
                MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER,
                MoviesContract.MovieEntry.COLUMN_MOVIE_RATING,
                MoviesContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS,
                MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE
        };

        return new CursorLoader(this, currentMovieUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
       if (cursor.getCount() < 1) {
            return;
        }
       if(cursor.moveToFirst()){
            int movieTitleColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_NAME);
            int moviePosterColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER);
            int movieRatingColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_RATING);
            int movieSynopsisColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS);
            int movieReleaseColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
           //
         /*  int movieTrailerColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_TRAILER);
           String movieTrailer = cursor.getString(movieTrailerColumnIndex);
           String[] movieTrailerArray = movieTrailer.split("\\s");
           ArrayAdapter<String> trailerAdapter = new ArrayAdapter<String>(this,
                   android.R.layout.simple_list_item_1, movieTrailerArray);
                     mTrailerListView.setAdapter(trailerAdapter);*/




           //
            String name = cursor.getString(movieTitleColumnIndex);
            String rating = cursor.getString(movieRatingColumnIndex);
            String synopsis = cursor.getString(movieSynopsisColumnIndex);
            String releaseDate = cursor.getString(movieReleaseColumnIndex);
            byte[] image = cursor.getBlob(moviePosterColumnIndex);
            Bitmap imageView = BitmapFactory.decodeByteArray(image, 0, image.length);
            mFavouritesMovieTitle.setText(name);
            mFavouritesMovieRating.setText(rating);
            mFavouritesMovieReleaseDate.setText(releaseDate);
            mFavouritesMovieSynopsis.setText(synopsis);
            mFavouritesMovieImage.setImageBitmap(imageView);



        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavouritesMovieTitle.setText("");
        mFavouritesMovieRating.setText("");
        mFavouritesMovieReleaseDate.setText("");
        mFavouritesMovieSynopsis.setText("");
        mFavouritesMovieImage.setImageBitmap(null);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(FavouritesDetail.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }


}
