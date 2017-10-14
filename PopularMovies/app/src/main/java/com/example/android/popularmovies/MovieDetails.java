package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import static android.R.attr.button;
import static android.R.attr.languageTag;
import static com.example.android.popularmovies.MainActivity.MOVIES_URL_LINK_FINAL;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieData>> {
    TextView mMovieTitle;
    ImageView mMovieImageView;
    TextView mReleaseDate;
    TextView mVoteAverage;
    TextView mPlotSynopsis;
    public static String month = "";
    public static String year = "";
    public static String date = "";
    String movieTitle;
    String movieReleaseDate;
    String moviePosterPath;
    String moviePlotSynopsis;
    String voteAverage;
    String imagePosterLink;
    String movieIdString;
    String mGridLink;
    String mBackDropLink;

    ToggleButton mFavouritesButton;

    public static String FINAL_DATA_URL;

    ListView mTrailerListView;
    MovieDataAdapter movieDataAdapter;
    MovieReviewAdapter moviewReviewAdapter;
    ListView mReviewListView;
    public  boolean mFavourite;
    public String reviewArray[] ;
    public String trailerArray[];


    String MOVIE_REVIEW_URL;
    String MOVIE_VIDEOS_URL;

    private static final int TRAILER_LOADER_ID = 0;
    private static final int REVIEW_LOADER_ID = 1;

    Uri currentMovieUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setTitle(R.string.movieDetails);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        currentMovieUri = intent.getData();

        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        mPlotSynopsis = (TextView) findViewById(R.id.movie_plot_synopsis);
        mVoteAverage = (TextView) findViewById(R.id.movie_vote_average);
        mMovieImageView = (ImageView) findViewById(R.id.movie_poster);
        mFavouritesButton = (ToggleButton) findViewById(R.id.favouritesButton);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            movieTitle = bundle.getString("movieTitle");
            movieReleaseDate = bundle.getString("movieReleaseDate");
            moviePosterPath = bundle.getString("moviePoster");
            moviePlotSynopsis = bundle.getString("plotSynopsis");
            voteAverage = bundle.getString("stringVoteAverage");
            ////////////
            movieIdString = bundle.getString("movieId");
            ///////////
            voteAverage = voteAverage + "/10";
            imagePosterLink = "http://image.tmdb.org/t/p/w500/" + moviePosterPath;
            movieReleaseDate = formatDate(movieReleaseDate);
            mFavouritesButton.setChecked(mFavourite);
            mFavouritesButton.setOnCheckedChangeListener(new MyCheckedChangeListener());
            mMovieTitle.setText(movieTitle);
            mPlotSynopsis.setText(moviePlotSynopsis);
            mVoteAverage.setText(voteAverage);
            mReleaseDate.setText(movieReleaseDate);
            Picasso.with(this).load(imagePosterLink).into(mMovieImageView);
            Log.v("MovieDetails", ":" + currentMovieUri);
        }
        mFavouritesButton.setChecked(mFavourite);
        mFavouritesButton.setOnCheckedChangeListener(new MyCheckedChangeListener());
        MOVIE_REVIEW_URL = "https://api.themoviedb.org/3/movie/" +
                movieIdString + "/reviews?api_key=403aab985eeaff498624b420c392e97c";
        MOVIE_VIDEOS_URL = "https://api.themoviedb.org/3/movie/" +
                movieIdString + "/videos?api_key=403aab985eeaff498624b420c392e97c";

    /*  mFavouritesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
               if(isChecked){

                   mFavourite = true;


               }else{
                   Toast.makeText(getApplicationContext(),"Movie removed from favourites",Toast.LENGTH_SHORT).show();
               }

           }
       });*/

        FINAL_DATA_URL = MOVIE_VIDEOS_URL;
        mTrailerListView = (ListView) findViewById(R.id.trailer_list_view);
        getLoaderManager().initLoader(TRAILER_LOADER_ID, null, this).forceLoad();
        movieDataAdapter = new MovieDataAdapter(this, new ArrayList<MovieData>());
        mTrailerListView.setAdapter(movieDataAdapter);




        mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieData currentMovie = movieDataAdapter.getItem(position);
                String movieLink = currentMovie.getmMovieData();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + movieLink));
                startActivity(intent);
            }
        });

        FINAL_DATA_URL = MOVIE_REVIEW_URL;
        mReviewListView = (ListView) findViewById(R.id.review_list_view);

        getLoaderManager().initLoader(REVIEW_LOADER_ID, null, this).forceLoad();
        moviewReviewAdapter = new MovieReviewAdapter(this, new ArrayList<MovieData>());
        mReviewListView.setAdapter(moviewReviewAdapter);


    }

    private class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String gridStr = "Grid";
            //String backdropStr = "Backdrop";
            if (mFavourite != isChecked && isChecked) {
                mFavourite = true;
               Drawable posterImageView = mMovieImageView.getDrawable();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) posterImageView);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                byte[] posterImageInByte = stream.toByteArray();
             /*  mGridLink = QueryUtils.storeImage(getApplicationContext(), mGridLink, getFilesDir().getAbsolutePath(), movieIdString,
                        gridStr);*/
               /* mBackDropLink = QueryUtils.storeImage(getApplicationContext(), mBackDropLink,
                        getFilesDir().getAbsolutePath(), movieIdString,
                        backdropStr);*/

                String movieTrailer = "";
                for(int i = 0 ; i<movieDataAdapter.getCount();i++){
                    MovieData currentMovie = movieDataAdapter.getItem(i);
                    String movieLink = currentMovie.getmMovieData();
                    movieTrailer = movieTrailer+" "+movieLink;
                }


                ContentValues values = new ContentValues();
                values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movieIdString);
                values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_NAME, movieTitle);
            //   values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_GRID_LINK, mGridLink);
                values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER, posterImageInByte);
                values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_RATING, voteAverage);
                values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, moviePlotSynopsis);
                values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TRAILER, movieTrailer);
                values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieReleaseDate);
                Uri newUri = getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values);

                if (newUri == null) {
                    // If the row ID is -1, then there was an error with insertion.
                    Toast.makeText(getApplicationContext(), "Error with saving", Toast.LENGTH_SHORT).show();

                } else {
                    // Otherwise, the insertion was successful and we can display a toast with the row ID.
                    //Toast.makeText(getApplicationContext(),"Movie added to favourites",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), movieTitle + " added to favourites", Toast.LENGTH_SHORT).show();
                }
            } else {
                mFavourite = false;
                int rowsDeleted = 0;
                //  mFavouritesButton.setChecked(false);
                //QueryUtils.deleteImage(getFilesDir().getAbsolutePath(),
                  //      movieIdString, gridStr);
                //QueryUtils.deleteImage(getFilesDir().getAbsolutePath(),
                  //     movieIdString, backdropStr);

                String selectionArgs[] = {movieIdString};

                rowsDeleted = getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI,
                        MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        selectionArgs);
             //  rowsDeleted = getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI,null,null);
                Log.v("MovieDetails", "rowsDeleted!!!!!!!!!!!!!!!!!:" + rowsDeleted);
                if (rowsDeleted == 0) {
                    // If no rows were deleted, then there was an error with the delete.
                    Toast.makeText(getApplicationContext(), "No rows deleted", Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the delete was successful and we can display a toast.
                    Toast.makeText(getApplicationContext(), "Movie Removed From Favourites: " + rowsDeleted, Toast.LENGTH_SHORT).show();
                }

        /*                Toast.makeText(getApplicationContext(),mMovieTitle + " removed from favourites",
                       Toast.LENGTH_SHORT).show();*/
            }

        }


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
            Intent settingsIntent = new Intent(MovieDetails.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static String formatDate(String movieReleaseDate) {
        char releaseDateArray[] = movieReleaseDate.toCharArray();
        for (int i = 0; i < 4; i++) {
            year = year + releaseDateArray[i];
        }
        for (int i = 8; i <= 9; i++) {
            date = date + releaseDateArray[i];
        }
        for (int i = 5; i < 6; i++) {
            if (i == 5) {
                if (releaseDateArray[i] == '1') {
                    switch (releaseDateArray[i + 1]) {
                        case '0': {
                            month = "October";
                            break;
                        }
                        case '1': {
                            month = "November";
                            break;
                        }
                        case '2': {
                            month = "December";
                            break;
                        }

                    }
                }
                if (releaseDateArray[i] == '0') {
                    switch (releaseDateArray[i + 1]) {
                        case '1': {
                            month = "January";
                            break;
                        }
                        case '2': {
                            month = "February";
                            break;
                        }
                        case '3': {
                            month = "March";
                            break;
                        }
                        case '4': {
                            month = "April";
                            break;
                        }
                        case '5': {
                            month = "May";
                            break;
                        }
                        case '6': {
                            month = "June";
                            break;
                        }
                        case '7': {
                            month = "July";
                            break;
                        }
                        case '8': {
                            month = "August";
                            break;
                        }
                        case '9': {
                            month = "September";
                            break;
                        }


                    }
                }

            }
        }
        char dateArray[] = date.toCharArray();
        if (dateArray[1] == '2') {
            movieReleaseDate = date + "nd-" + month + "-" + year;
        } else if (dateArray[1] == '1') {
            movieReleaseDate = date + "st-" + month + "-" + year;
        } else {
            movieReleaseDate = date + "th-" + month + "-" + year;
        }

        date = "";
        month = "";
        year = "";
        return movieReleaseDate;
    }


    @Override
    public Loader<List<MovieData>> onCreateLoader(int i, Bundle bundle) {
        return new MovieDataLoader(this, FINAL_DATA_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<MovieData>> loader, List<MovieData> movieDatas) {
        if (loader.getId() == TRAILER_LOADER_ID) {
            movieDataAdapter.addAll(movieDatas);
        } else if (loader.getId() == REVIEW_LOADER_ID) {
            moviewReviewAdapter.addAll(movieDatas);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieData>> loader) {
        movieDataAdapter.clear();
        moviewReviewAdapter.clear();
    }

    public static void insert() {


    }

    public interface CallBack {
        void onFavouriteClicked();
    }

    public class MovieCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

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
            return new CursorLoader(getApplicationContext(), currentMovieUri, projection, null, null, null);

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
