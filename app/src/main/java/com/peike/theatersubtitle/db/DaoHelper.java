package com.peike.theatersubtitle.db;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.MovieDao;
import com.peike.theatersubtitle.detail.SearchSubtitleTask;
import com.peike.theatersubtitle.home.GetHotMovieTask;

import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;

public class DaoHelper {

    public List<Movie> getHotMovieList() {
        DaoSession daoSession = AppApplication.getDaoSession();
        MovieDao movieDao = daoSession.getMovieDao();
        return movieDao.queryBuilder().list();
    }

    public Movie getMovie(String imdbId) {
        DaoSession daoSession = AppApplication.getDaoSession();
        MovieDao movieDao = daoSession.getMovieDao();
        return movieDao.queryBuilder()
                .where(MovieDao.Properties.ImdbId.eq(imdbId))
                .unique();
    }

    public List<Subtitle> getSubtitles(String imdbId) {
        DaoSession daoSession = AppApplication.getDaoSession();
        SubtitleDao subtitleDao = daoSession.getSubtitleDao();
        return subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.ImdbId.eq(imdbId))
                .list();
    }

    public void deleteSubtitleByImdbId(String imdbId) {
        DaoSession daoSession = AppApplication.getDaoSession();
        SubtitleDao subtitleDao = daoSession.getSubtitleDao();
        DeleteQuery deleteQuery = subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.ImdbId.eq(imdbId))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

}
