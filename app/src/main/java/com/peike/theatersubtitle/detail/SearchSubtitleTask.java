package com.peike.theatersubtitle.detail;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ApiAsyncTask;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.api.Result;
import com.peike.theatersubtitle.api.SubtitleService;
import com.peike.theatersubtitle.api.model.SubtitleResponse;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.db.SubtitleDao;

import java.io.IOException;
import java.util.List;

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
            subtitleDao.deleteAll();
            subtitleDao.insertOrReplaceInTx(subtitleList);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }
}
