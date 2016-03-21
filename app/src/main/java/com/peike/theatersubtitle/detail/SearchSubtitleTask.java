package com.peike.theatersubtitle.detail;

import android.util.Log;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ApiAsyncTask;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.api.Result;
import com.peike.theatersubtitle.api.SubtitleService;
import com.peike.theatersubtitle.api.model.SubtitleResponse;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.db.SubtitleDao;
import com.peike.theatersubtitle.util.Constants;

import java.io.IOException;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import retrofit2.Call;

/**
 * param[0] is IMDb ID
 * param[1] is comma separated ISO639_2 language code
 */
public class SearchSubtitleTask extends ApiAsyncTask<String> {

    private static final String TAG = SearchSubtitleTask.class.getSimpleName();

    public SearchSubtitleTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(String... params) {
        String imdbId = params[0];
        String languageCodes = params[1];
        SubtitleService subtitleService = getSubtitleService();
        Call<List<SubtitleResponse>> call = subtitleService.searchSubtitle(imdbId, languageCodes);
        try {
            List<SubtitleResponse> subtitleResponses = call.execute().body();
            List<Subtitle> subtitleList = convertList(subtitleResponses, Subtitle.class);
            SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
            subtitleDao.insertOrReplaceInTx(subtitleList);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }

    private boolean hasValidCache(String imdbId) {
        boolean hasValidCache;
        SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
        List<Subtitle> resultCache = subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.ImdbId.eq(imdbId))
                .list();
        if (resultCache.isEmpty()) {
            hasValidCache = false;
        } else {
            hasValidCache = true;
        }
        return hasValidCache;
    }

    private void deleteCache(SubtitleDao subtitleDao, String imdbId) {
        DeleteQuery deleteQuery = subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.ImdbId.eq(imdbId))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        Log.d(TAG, "Subtitle cache deleted: " + imdbId);
    }
}
