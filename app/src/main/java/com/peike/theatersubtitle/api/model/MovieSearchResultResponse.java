package com.peike.theatersubtitle.api.model;

import com.google.gson.annotations.SerializedName;

public class MovieSearchResultResponse implements Response{
    @SerializedName("title")
    public String title;
    @SerializedName(value = "posterUrl", alternate = {"poster_url"})
    public String posterUrl;
    @SerializedName(value = "backdropUrl", alternate = {"backdrop_url"})
    public String backdropUrl;
    @SerializedName(value = "imdbId", alternate = {"imdb_id"})
    public String imdbId;
    @SerializedName(value = "imdbRating", alternate = {"imdb_rating"})
    public String imdbRating;
    @SerializedName(value = "tomatoRating", alternate = {"tomato_meter"})
    public String tomatoRating;
    public String query;
    public Long expiredAt;
}
