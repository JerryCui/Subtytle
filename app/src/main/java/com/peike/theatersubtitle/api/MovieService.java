package com.peike.theatersubtitle.api;

import com.peike.theatersubtitle.db.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie")
    Call<List<Movie>> listHotMovies();

    @GET("movie/search")
    Call<List<Movie>> searchMovie(@Query("query") String query);
}
