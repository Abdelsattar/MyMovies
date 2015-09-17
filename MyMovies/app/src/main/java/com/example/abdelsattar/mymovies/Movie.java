package com.example.abdelsattar.mymovies;

/**
 * Created by abdelsattar on 09/09/15.
 */
public class Movie {
    private  String title ;
    private String overview;
    private String backgroundUrl;
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


}
