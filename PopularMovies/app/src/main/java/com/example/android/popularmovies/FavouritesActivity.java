package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDbHelper;

import static android.R.attr.id;
import static android.os.Build.VERSION_CODES.M;

public class FavouritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    FavouritesAdapter1 mFavoritesAdapter;
    GridView mFavouritesGridView;
    private static int FAVOURITES_LOADER = 0;
    MoviesDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFavouritesGridView = (GridView) findViewById(R.id.grid_favourites);

          getLoaderManager().initLoader(FAVOURITES_LOADER,null,this);
        mDbHelper = new MoviesDbHelper(this);
        mFavoritesAdapter = new FavouritesAdapter1(this,null);

        if(mFavoritesAdapter == null){
            mFavouritesGridView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"no movies in favourites", Toast.LENGTH_SHORT).show();
        }

        mFavouritesGridView.setAdapter(mFavoritesAdapter);

       mFavouritesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
               //create a new intent
               Intent intent = new Intent(FavouritesActivity.this,FavouritesDetail.class);
               Uri currentInventoryUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI,id);
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
        if (id == android.R.id.home) {
            onBackPressed();
        }
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(FavouritesActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String projection[] = {
                MoviesContract.MovieEntry._ID,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
                MoviesContract.MovieEntry.COLUMN_MOVIE_NAME,
                MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER,
                //MoviesContract.MovieEntry.COLUMN_MOVIE_GRID_LINK,
                MoviesContract.MovieEntry.COLUMN_MOVIE_RATING,
                MoviesContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS,
                MoviesContract.MovieEntry.COLUMN_MOVIE_TRAILER,
                MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE
        };
        return new CursorLoader(this,MoviesContract.MovieEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mFavoritesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoritesAdapter.swapCursor(null);
    }
}
