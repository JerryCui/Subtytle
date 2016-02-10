package com.peike.theatersubtitle.home;

import android.os.AsyncTask;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.MovieService;
import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.MovieDao;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetHotMovieTask extends AsyncTask<Void, Void, Void> {
    private static final String URL = "https://aqueous-falls-1653.herokuapp.com";
    @Override
    protected Void doInBackground(Void... params) {
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
        }
        return null;
    }
}
