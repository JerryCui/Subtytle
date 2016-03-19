package com.peike.theatersubtitle.db;

import com.google.gson.Gson;
import com.peike.theatersubtitle.AppApplication;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;

public class DaoHelper {

    public List<Movie> getHotMovieList() {
        MovieDao movieDao = AppApplication.getMovieDao();
        return movieDao.queryBuilder()
                .where(MovieDao.Properties.BoxOffice.isNotNull())
                .list();
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

    public void markSubtitleDownloaded(String fileId) {
        SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
        Subtitle subtitle = subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.FileId.eq(fileId))
                .unique();
        subtitle.setDownloaded(true);
        subtitleDao.update(subtitle);
    }

    public List<Movie> getLocalMovies() {
        SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
        List<Subtitle> downloadedSubtitles = subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.Downloaded.eq(true))
                .list();
        List<String> imdbList = new ArrayList<>();
        for (Subtitle subtitle : downloadedSubtitles) {
            imdbList.add(subtitle.getImdbId());
        }
        MovieDao movieDao = AppApplication.getMovieDao();
        return movieDao.queryBuilder()
                .where(MovieDao.Properties.ImdbId.in(imdbList))
                .list();
    }

    public void insertToMovie(MovieSearchResult searchResult) {
        Gson gson = new Gson();
        String intermediary = gson.toJson(searchResult);
        Movie movie = gson.fromJson(intermediary, Movie.class);
        movie.setId(null);
        MovieDao movieDao = AppApplication.getMovieDao();
        movieDao.insertOrReplace(movie);
    }
}
