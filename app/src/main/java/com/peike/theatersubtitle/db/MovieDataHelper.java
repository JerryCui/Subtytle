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

public class MovieDataHelper {

    public void initGetHotMovieTask(ResponseListener responseListener) {
        new GetHotMovieTask(responseListener).execute();
    }

    /**
     *
     * @param responseListener
     * @param imdbID
     * @param languages comma separated ISO639_2 language code
     */
    public void initSearchSubtitleTask(ResponseListener responseListener, String imdbID, String languages) {
        new SearchSubtitleTask(responseListener).execute(imdbID, languages);
    }

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

    public void deleteSubtitleByImdbId(String imdbId) {
        DaoSession daoSession = AppApplication.getDaoSession();
        SubtitleDao subtitleDao = daoSession.getSubtitleDao();
        DeleteQuery deleteQuery = subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.ImdbId.eq(imdbId))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

}
