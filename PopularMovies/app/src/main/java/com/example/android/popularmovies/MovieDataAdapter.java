package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RAHUL YADAV on 18-03-2017.
 */

public class MovieDataAdapter extends ArrayAdapter<MovieData> {
    Context mContext;
    public MovieDataAdapter(Context context, ArrayList<MovieData> movie){
        super(context,0,movie);
        mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listDataView = convertView;
        if(listDataView == null){
            listDataView = LayoutInflater.from(getContext()).inflate(R.layout.movie_data_item,parent,false);
        }
        MovieData currentMovieData = getItem(position);
       // TextView movieDataTextView = (TextView)listDataView.findViewById(R.id.movie_data_item_id);
        TextView movieDataTextName = (TextView)listDataView.findViewById(R.id.data_name);
        String movieData =  currentMovieData.getmMovieData();
        String movieTrialerName = currentMovieData.getmName();
        //movieDataTextView.setText(movieData);
        movieDataTextName.setText(movieTrialerName);

        return listDataView;
    }
}
