package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by RAHUL YADAV on 06-04-2017.
 */

public class MovieParcel implements Parcelable {
    private String name, backdrop_link, synopsis, avg, rel_date, movieId, grid_link;

    public MovieParcel(String name, String backdrop_link, String synopsis, String avg,
                       String rel_date, String movieId, String grid_link) {
        this.name = name;
        this.backdrop_link = backdrop_link;
        this.synopsis = synopsis;
        this.avg = avg;
        this.rel_date = rel_date;
        this.movieId = movieId;
        this.grid_link = grid_link;
    }

    public MovieParcel(Parcel source) {
        name = source.readString();
        backdrop_link = source.readString();
        synopsis = source.readString();
        avg = source.readString();
        rel_date = source.readString();
        movieId = source.readString();
        grid_link = source.readString();
    }



    public static final Creator<MovieParcel> CREATOR = new Creator<MovieParcel>() {
        @Override
        public MovieParcel createFromParcel(Parcel source) {
            return new MovieParcel(source);
        }

        @Override
        public MovieParcel[] newArray(int size) {
            return new MovieParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(backdrop_link);
        parcel.writeString(synopsis);
        parcel.writeString(avg);
        parcel.writeString(rel_date);
        parcel.writeString(movieId);
        parcel.writeString(grid_link);
    }
    public HashMap<String, String> getHash() {
        HashMap<String, String> details = new HashMap<>();
        details.put("Movie Name", name);
        details.put("Backdrop Link", backdrop_link);
        details.put("Synopsis", synopsis);
        details.put("Rating", avg);
        details.put("Release Date", rel_date);
        details.put("Movie Id", movieId);
        details.put("Grid Link", grid_link);

        return details;
    }
}
