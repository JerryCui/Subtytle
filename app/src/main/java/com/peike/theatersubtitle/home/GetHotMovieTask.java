package com.peike.theatersubtitle.home;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ApiAsyncTask;
import com.peike.theatersubtitle.api.MovieService;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.api.Result;
import com.peike.theatersubtitle.api.model.MovieResponse;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.MovieDao;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class GetHotMovieTask extends ApiAsyncTask<Void> {

    public GetHotMovieTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(Void... params) {
        MovieService movieService = getMovieService();
        Call<List<MovieResponse>> call = movieService.listHotMovies();
        try {
            List<MovieResponse> movieResponses = call.execute().body();
            List<Movie> movieList = convertList(movieResponses, Movie.class);
            MovieDao movieDao = AppApplication.getMovieDao();
            movieDao.insertOrReplaceInTx(movieList);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }
}
