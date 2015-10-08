package com.example.abdelsattar.mymovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abdelsattar on 09/09/15.
 */
public class Movie implements Parcelable {
    private  String title ;
    private String overview;
    private String backgroundUrl;

    public Movie(Parcel in) {
        this.title = in.readString();
        this.overview = in.readString();
        this.backgroundUrl = in.readString();
        this.posterURL = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
        this.movieID = in.readString();
    }

    private String posterURL;
    private String rating;
    private String releaseDate;

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getMovieID() {
        return movieID;
    }

    private String movieID;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Movie() {
        super();
    }

    public void clear(){
        this.title = null ;
        this.overview =null;
        this.backgroundUrl =null;
        this.posterURL = null;
        this.rating= null;
        this.releaseDate =null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.overview);
        parcel.writeString(this.backgroundUrl);
        parcel.writeString(this.posterURL);
        parcel.writeString(this.rating);
        parcel.writeString(this.releaseDate);

    }
    @Override
    public String toString() {
        return title + "  "
                + overview + "  "
                + rating + "  "
                + backgroundUrl + "  "
                + releaseDate + "  ";
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
