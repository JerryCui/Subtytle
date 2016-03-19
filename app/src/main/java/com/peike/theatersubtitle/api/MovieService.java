package com.peike.theatersubtitle.api;

import com.peike.theatersubtitle.api.model.MovieResponse;
import com.peike.theatersubtitle.api.model.MovieSearchResultResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie")
    Call<List<MovieResponse>> listHotMovies();

    @GET("movie/search")
    Call<List<MovieSearchResultResponse>> searchMovie(@Query("query") String query);
}
