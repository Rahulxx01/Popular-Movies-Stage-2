package com.example.android.popularmovies;

/**
 * Created by RAHUL YADAV on 18-02-2017.
 */

public class Movie {
    private String mMovieTitle;
    private String mMovieReleaseDate;
    private String mMoviePlotSynopsis;
    private Double mMovieVoteAverage;
    private String mMoviePosterPath;
    private int mMovieID;
    private String mMovieTrailer;
    private String mMovieReview;

    public Movie(int movieID, String movieTitle, String movieReleaseDate, String moviePlotSynopsis, String moviePosterPath, Double movieVoteAverage) {
        mMovieID = movieID;
        mMovieTitle = movieTitle;
        mMovieReleaseDate = movieReleaseDate;
        mMoviePlotSynopsis = moviePlotSynopsis;
        mMoviePosterPath = moviePosterPath;
        mMovieVoteAverage = movieVoteAverage;

    }


    public String getmMovieTrailer() {
        return mMovieTrailer;
    }

    public String getmMovieReview() {
        return mMovieReview;
    }

    public int getmMovieID() {
        return mMovieID;
    }

    public Double getmMovieVoteAverage() {
        return mMovieVoteAverage;
    }

    public String getmMoviePlotSynopsis() {
        return mMoviePlotSynopsis;
    }

    public String getmMoviePosterPath() {
        return mMoviePosterPath;
    }

    public String getmMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public String getmMovieTitle() {
        return mMovieTitle;
    }
}
