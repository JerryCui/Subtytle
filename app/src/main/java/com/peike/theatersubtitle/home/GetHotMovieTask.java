package com.peike.theatersubtitle.home;

import android.os.AsyncTask;

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

public class GetHotMovieTask extends ApiAsyncTask<Void, Void, Result> {

    public GetHotMovieTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(Void... params) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService movieService = retrofit.create(MovieService.class);
        Call<List<Movie>> call = movieService.listHotMovies();
        try {
            Response<List<Movie>> response = call.execute();
            List<Movie> movies = response.body();
            DaoSession daoSession = AppApplication.getDaoSession();
            MovieDao movieDao = daoSession.getMovieDao();
            movieDao.insertInTx(movies);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }

    @Override
    protected void onPostExecute(Result result) {
        switch (result) {
            case SUCCESS:
                responseListener.onSuccess();
                break;
            case FAIL:
                responseListener.onFailure();
                break;
            default:
                break;
        }
    }
}
