package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDbHelper;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.media.tv.TvContract.Programs.Genres.MOVIES;
import static android.os.Build.VERSION_CODES.M;
import static android.view.View.GONE;
import static com.example.android.popularmovies.QueryUtils.LOG_TAG;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String MOVIES_URL_LINK = "https://api.themoviedb.org/3/movie/popular?api_key=403aab985eeaff498624b420c392e97c";
    public static final String MOVIES_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=403aab985eeaff498624b420c392e97c";
    public static String MOVIES_URL_LINK_FINAL;// MOVIES_URL_LINK;
    private static final int MOVIE_ID = 1;
    public MovieAdapter movieAdapter;
    public GridView movieGridView;
    private TextView mEmptyStateTexView;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    MoviesDbHelper mDbHelper;
    private static int FAVOURITES_LOADER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEmptyStateTexView = (TextView) findViewById(R.id.empty_view);
        movieGridView = (GridView) findViewById(R.id.grid);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
                startActivity(intent);
            }
        });
        boolean flag = checkInternetConnectivity();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        String prefUrlByUser = MoviePreferences.getPreferenceMovieUrlString(this);
        if (flag) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_ID, null, this).forceLoad();
            if (prefUrlByUser.equals(getString(R.string.pref_unit_mostpopular))) {
                MOVIES_URL_LINK_FINAL = MOVIES_URL_LINK;
                View loadingIndicator = (View) findViewById(R.id.progress_bar);
                loadingIndicator.setVisibility(View.VISIBLE);
                getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
                mEmptyStateTexView.setVisibility(View.INVISIBLE);
            } else if (prefUrlByUser.equals(getString(R.string.pref_units_toprated))) {
                MOVIES_URL_LINK_FINAL = MOVIES_TOP_RATED;
                View loadingIndicator = (View) findViewById(R.id.progress_bar);
                loadingIndicator.setVisibility(View.VISIBLE);
                getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
                mEmptyStateTexView.setVisibility(View.INVISIBLE);
            }
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible//
            View loadingIndicator = (View) findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(GONE);
            mEmptyStateTexView.setVisibility(View.VISIBLE);
            Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
            startActivity(intent);

        }
        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        movieGridView.setAdapter(movieAdapter);
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie currentMovie = movieAdapter.getItem(position);
                String movieTitle = currentMovie.getmMovieTitle();
                String movieReleaseDate = currentMovie.getmMovieReleaseDate();
                String moviePoster = currentMovie.getmMoviePosterPath();
                String plotSynopsis = currentMovie.getmMoviePlotSynopsis();
                Double voteAverage = currentMovie.getmMovieVoteAverage();
                String stringVoteAverage = Double.toString(voteAverage);
                String movieId = Integer.toString(currentMovie.getmMovieID());
                Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("movieTitle", movieTitle);
                bundle.putString("movieReleaseDate", movieReleaseDate);
                bundle.putString("moviePoster", moviePoster);
                bundle.putString("plotSynopsis", plotSynopsis);
                bundle.putString("stringVoteAverage", stringVoteAverage);
                bundle.putString("movieId", movieId);
                intent.putExtras(bundle);
                Uri currentInventoryUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI, id);
                intent.setData(currentInventoryUri);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this, MOVIES_URL_LINK_FINAL);
    }
    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        movieAdapter.clear();
        View loadingIndicator = (View) findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(GONE);
        if (movieAdapter != null && movies != null) {
            movieAdapter.addAll(movies);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieAdapter.clear();
    }

    public boolean checkInternetConnectivity() {
        //Check internet connection//
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network//
        NetworkInfo netInformation = connectivityManager.getActiveNetworkInfo();
        // If there is a network connection, then fetch data//
        if (netInformation != null && netInformation.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(LOG_TAG, "onStart: preferences were updated");
            String prefUrlByUser = MoviePreferences.getPreferenceMovieUrlString(this);
            if (prefUrlByUser.equals(getString(R.string.pref_unit_mostpopular))) {
                MOVIES_URL_LINK_FINAL = MOVIES_URL_LINK;
                getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
                mEmptyStateTexView.setVisibility(View.INVISIBLE);
            } else if (prefUrlByUser.equals(getString(R.string.pref_units_toprated))) {
                MOVIES_URL_LINK_FINAL = MOVIES_TOP_RATED;
                getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
                mEmptyStateTexView.setVisibility(View.INVISIBLE);
            }
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (movieGridView.getAdapter() == null) {

        }
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(LOG_TAG, "onStart: preferences were updated");
            String prefUrlByUser = MoviePreferences.getPreferenceMovieUrlString(this);
            if (prefUrlByUser.equals(getString(R.string.pref_unit_mostpopular))) {
                MOVIES_URL_LINK_FINAL = MOVIES_URL_LINK;
                getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
                mEmptyStateTexView.setVisibility(View.INVISIBLE);
            } else if (prefUrlByUser.equals(getString(R.string.pref_units_toprated))) {
                MOVIES_URL_LINK_FINAL = MOVIES_TOP_RATED;
                getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
                mEmptyStateTexView.setVisibility(View.INVISIBLE);
            }
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }


}
