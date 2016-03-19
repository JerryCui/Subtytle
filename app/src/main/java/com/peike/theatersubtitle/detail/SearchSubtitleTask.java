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

import de.greenrobot.dao.query.DeleteQuery;
import retrofit2.Call;

/**
 * param[0] is IMDb ID
 * param[1] is comma separated ISO639_2 language code
 */
public class SearchSubtitleTask extends ApiAsyncTask<String> {

    public SearchSubtitleTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(String... params) {
        SubtitleService subtitleService = getSubtitleService();
        Call<List<SubtitleResponse>> call = subtitleService.searchSubtitle(params[0], params[1]);
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
}
