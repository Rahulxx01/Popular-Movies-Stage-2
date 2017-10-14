package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by RAHUL YADAV on 05-04-2017.
 */

public class FavouritesAdapter1 extends CursorAdapter {
    Context mContext;

    public FavouritesAdapter1(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_favourites_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.favourites_grid_image_poster);
        int imageUrlColumnIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER);
      //imageUrlColumnIndex = 0;


        if(imageUrlColumnIndex == -1){
            Toast.makeText(context,"No movies in favourites", Toast.LENGTH_SHORT).show();
            return;
        }
      //  String imageLink = cursor.getString(imageUrlColumnIndex);
        byte[] image = cursor.getBlob(imageUrlColumnIndex);
        Bitmap mImageView = BitmapFactory.decodeByteArray(image, 0, image.length);

        imageView.setImageBitmap(mImageView);

        //Picasso.with(mContext).load(Uri.parse(imageLink)).into(imageView);


    }
}

