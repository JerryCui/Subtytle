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
public class SearchSubtitleTask extends ApiAsyncTask<String, Void, Result> {

    public SearchSubtitleTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(String... params) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SubtitleService subtitleService = retrofit.create(SubtitleService.class);
        Call<List<Subtitle>> call = subtitleService.searchSubtitle(params[0], params[1]);
        try {
            Response<List<Subtitle>> response = call.execute();
            List<Subtitle> subtitles = response.body();
            DaoSession daoSession = AppApplication.getDaoSession();
            SubtitleDao subtitleDao = daoSession.getSubtitleDao();
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

    @Override
    protected void onPostExecute(Result result) {
        switch (result) {
            case SUCCESS:
                responseListener.onSuccess();
                break;
            case FAIL:
                responseListener.onFailure();
                break;
            default:
                break;
        }
    }
}
