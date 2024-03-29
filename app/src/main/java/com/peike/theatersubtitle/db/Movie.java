package com.peike.theatersubtitle.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MOVIE".
 */
public class Movie {

    private Long id;
    private String title;
    private String posterUrl;
    private String backdropUrl;
    /** Not-null value. */
    private String imdbId;
    private String imdbRating;
    private String tomatoRating;
    private String moviePlot;
    private String boxOffice;
    private Integer revision;

    public Movie() {
    }

    public Movie(Long id) {
        this.id = id;
    }

    public Movie(Long id, String title, String posterUrl, String backdropUrl, String imdbId, String imdbRating, String tomatoRating, String moviePlot, String boxOffice, Integer revision) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.backdropUrl = backdropUrl;
        this.imdbId = imdbId;
        this.imdbRating = imdbRating;
        this.tomatoRating = tomatoRating;
        this.moviePlot = moviePlot;
        this.boxOffice = boxOffice;
        this.revision = revision;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    /** Not-null value. */
    public String getImdbId() {
        return imdbId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getTomatoRating() {
        return tomatoRating;
    }

    public void setTomatoRating(String tomatoRating) {
        this.tomatoRating = tomatoRating;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public void setMoviePlot(String moviePlot) {
        this.moviePlot = moviePlot;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

}
