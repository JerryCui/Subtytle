package com.peike.theatersubtitle.home;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ApiAsyncTask;
import com.peike.theatersubtitle.api.MovieService;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.api.Result;
import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.MovieDao;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetHotMovieTask extends ApiAsyncTask<Void> {

    public GetHotMovieTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(Void... params) {
        MovieService movieService = getMovieService();
        Call<List<Movie>> call = movieService.listHotMovies();
        try {
            List<Movie> movies = call.execute().body();
            MovieDao movieDao = AppApplication.getMovieDao();
            movieDao.deleteAll();
            movieDao.insertInTx(movies);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }
}
