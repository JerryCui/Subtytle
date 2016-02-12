package com.peike.theatersubtitle.home;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.MovieDao;

import java.util.List;

public class MovieDataHelper {

    public void initGetHotMovieTast(ResponseListener responseListener) {
        new GetHotMovieTask(responseListener).execute();
    }

    public List<Movie> getHotMovieList() {
        DaoSession daoSession = AppApplication.getDaoSession();
        MovieDao movieDao = daoSession.getMovieDao();
        return movieDao.queryBuilder().list();
    }

}
