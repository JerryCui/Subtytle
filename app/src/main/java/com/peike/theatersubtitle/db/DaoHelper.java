package com.peike.theatersubtitle.db;

import com.google.gson.Gson;
import com.peike.theatersubtitle.AppApplication;

import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;

public class DaoHelper {

    public List<Movie> getHotMovieList() {
        MovieDao movieDao = AppApplication.getMovieDao();
        return movieDao.queryBuilder().list();
    }

    public Movie getMovie(String imdbId) {
        MovieDao movieDao = AppApplication.getMovieDao();
        return movieDao.queryBuilder()
                .where(MovieDao.Properties.ImdbId.eq(imdbId))
                .unique();
    }

    public List<Subtitle> getSubtitles(String imdbId) {
        SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
        return subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.ImdbId.eq(imdbId))
                .list();
    }

    public void deleteSubtitleByImdbId(String imdbId) {
        SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
        DeleteQuery deleteQuery = subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.ImdbId.eq(imdbId))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    public List<MovieSearchResult> getMovieSearchResult(String query) {
        MovieSearchResultDao movieSearchResultDao = AppApplication.getMovieSearchResultDao();
        return movieSearchResultDao.queryBuilder()
                .where(MovieSearchResultDao.Properties.Query.eq(query))
                .list();
    }

    public Movie getResultMovie(String selectedImdbId) {
        MovieSearchResultDao movieSearchResultDao = AppApplication.getMovieSearchResultDao();
        MovieSearchResult movieSearchResult = movieSearchResultDao.queryBuilder()
                .where(MovieSearchResultDao.Properties.ImdbId.eq(selectedImdbId))
                .unique();
        Gson gson = new Gson();
        String intermediary = gson.toJson(movieSearchResult);
        return gson.fromJson(intermediary, Movie.class);
    }

    public void markSubtitleDownloaded(String fileId) {
        SubtitleDao subtitleDao = AppApplication.getSubtitleDao();

    }
}
