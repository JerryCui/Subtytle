package com.peike.theatersubtitle.search;

import android.util.Log;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ApiAsyncTask;
import com.peike.theatersubtitle.api.MovieService;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.api.Result;
import com.peike.theatersubtitle.api.model.MovieSearchResultResponse;
import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.db.MovieSearchResult;
import com.peike.theatersubtitle.db.MovieSearchResultDao;
import com.peike.theatersubtitle.util.Constants;

import java.io.IOException;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import retrofit2.Call;

public class SearchMovieTask extends ApiAsyncTask<String> {

    private static final String TAG = SearchMovieTask.class.getSimpleName();

    public SearchMovieTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(String... params) {
        String query = params[0];
        if (hasValidCache(query)) {
            Log.d(TAG, "Has valid cache: " + query);
            return Result.SUCCESS;
        }
        MovieService movieService = getMovieService();
        Call<List<MovieSearchResultResponse>> call = movieService.searchMovie(query);
        try {
            List<MovieSearchResultResponse> resultResponses = call.execute().body();
            List<MovieSearchResult> resultList = convertList(resultResponses, MovieSearchResult.class);
            MovieSearchResultDao movieSearchResultDao = AppApplication.getMovieSearchResultDao();
            setMetadata(resultList, query, System.currentTimeMillis() + Constants.SEARCH_RESULT_CACHE_LIFE);
            movieSearchResultDao.insertInTx(resultList);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }

    private boolean hasValidCache(String query) {
        boolean hasValidCache;
        DaoSession daoSession = AppApplication.getDaoSession();
        MovieSearchResultDao movieSearchResultDao = daoSession.getMovieSearchResultDao();
        List<MovieSearchResult> resultCache = movieSearchResultDao.queryBuilder()
                .where(MovieSearchResultDao.Properties.Query.eq(query))
                .list();
        if (resultCache.isEmpty()) {
            hasValidCache = false;
        } else {
            long expiredAt = resultCache.get(0).getExpiredAt();
            if (expiredAt <= System.currentTimeMillis()) {
                deleteCache(movieSearchResultDao, query);
                hasValidCache = false;
            } else {
                hasValidCache = true;
            }
        }
        daoSession.clear();
        return hasValidCache;
    }

    private void deleteCache(MovieSearchResultDao movieSearchResultDao, String query) {
        Log.d(TAG, "Search cache deleted: " + query);
        DeleteQuery deleteQuery = movieSearchResultDao.queryBuilder()
                .where(MovieSearchResultDao.Properties.Query.eq(query))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    private void setMetadata(List<MovieSearchResult> movieList, String query, long expiredAt) {
        for (int i = 0; i < movieList.size(); ++i) {
            movieList.get(i).setQuery(query);
            movieList.get(i).setExpiredAt(expiredAt);
        }
    }
}
