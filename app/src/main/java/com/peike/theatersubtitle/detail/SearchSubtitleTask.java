package com.peike.theatersubtitle.detail;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ApiAsyncTask;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.api.Result;
import com.peike.theatersubtitle.api.SubtitleService;
import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.db.SubtitleDao;

import java.io.IOException;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * param[0] in execute is IMDb ID
 * param[1] is comma separated ISO639_2 language code
 */
public class SearchSubtitleTask extends ApiAsyncTask<String> {

    public SearchSubtitleTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(String... params) {
        SubtitleService subtitleService = getSubtitleService();
        Call<List<Subtitle>> call = subtitleService.searchSubtitle(params[0], params[1]);
        try {
            List<Subtitle> subtitles = call.execute().body();
            SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
            DeleteQuery deleteQuery = subtitleDao.queryBuilder()
                    .where(SubtitleDao.Properties.ImdbId.eq(params[0]))
                    .buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();
            subtitleDao.insertInTx(subtitles);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }
}
