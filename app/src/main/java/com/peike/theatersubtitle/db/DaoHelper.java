package com.peike.theatersubtitle.db;

import com.google.gson.Gson;
import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;

public class DaoHelper {

    public List<Movie> getHotMovieList() {
        MovieDao movieDao = AppApplication.getMovieDao();
        Movie movie = movieDao.queryBuilder()
                .orderDesc(MovieDao.Properties.Revision)
                .limit(1)
                .unique();
        if (movie == null) {
            return Collections.emptyList();
        } else {
            return movieDao.queryBuilder()
                    .where(MovieDao.Properties.Revision.eq(movie.getRevision()))
                    .list();
        }
    }

    public Movie getMovie(String imdbId) {
        MovieDao movieDao = AppApplication.getMovieDao();
        return movieDao.queryBuilder()
                .where(MovieDao.Properties.ImdbId.eq(imdbId))
                .unique();
    }


    public List<Subtitle> getCachedSubtitle(String imdbId) {
        SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
        return subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.ImdbId.eq(imdbId))
                .list();
    }

    public List<LocalSubtitle> getLocalSubtitle(String imdbId) {
        LocalSubtitleDao localSubtitleDao = AppApplication.getLocalSubtitleDao();
        return localSubtitleDao.queryBuilder()
                .where(LocalSubtitleDao.Properties.ImdbId.eq(imdbId))
                .list();
    }

    public void deleteSubtitleByImdbId(String imdbId) {
        LocalSubtitleDao localSubtitleDao = AppApplication.getLocalSubtitleDao();
        DeleteQuery deleteQuery = localSubtitleDao.queryBuilder()
                .where(LocalSubtitleDao.Properties.ImdbId.eq(imdbId))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    public List<MovieSearchResult> getMovieSearchResult(String query) {
        MovieSearchResultDao movieSearchResultDao = AppApplication.getMovieSearchResultDao();
        return movieSearchResultDao.queryBuilder()
                .where(MovieSearchResultDao.Properties.Query.eq(query))
                .list();
    }

    public void markSubtitleDownloaded(Subtitle subtitle) {
        Gson gson = new Gson();
        String intermediary = gson.toJson(subtitle);
        LocalSubtitle localSubtitle = gson.fromJson(intermediary, LocalSubtitle.class);
        LocalSubtitleDao localSubtitleDao = AppApplication.getLocalSubtitleDao();
        localSubtitleDao.insertOrReplace(localSubtitle);
    }

    public List<Movie> getLocalMovies() {
        LocalSubtitleDao localSubtitleDao = AppApplication.getLocalSubtitleDao();
        List<LocalSubtitle> downloadedSubtitles = localSubtitleDao.queryBuilder().list();
        List<String> imdbList = new ArrayList<>();
        for (LocalSubtitle localSubtitle : downloadedSubtitles) {
            imdbList.add(localSubtitle.getImdbId());
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

    public boolean isLocalSubtitle(Integer fileId) {
        LocalSubtitleDao localSubtitleDao = AppApplication.getLocalSubtitleDao();
        long count = localSubtitleDao.queryBuilder()
                .where(LocalSubtitleDao.Properties.FileId.eq(fileId))
                .count();
        return count > 0;
    }

    public boolean hasLocalSubtitle(String imdbId) {
        LocalSubtitleDao localSubtitleDao = AppApplication.getLocalSubtitleDao();
        return localSubtitleDao.queryBuilder()
                .where(LocalSubtitleDao.Properties.ImdbId.eq(imdbId))
                .count() > 0;
    }

    public void deleteAllLocalSubtitle() {
        LocalSubtitleDao localSubtitleDao = AppApplication.getLocalSubtitleDao();
        localSubtitleDao.deleteAll();
    }
}
