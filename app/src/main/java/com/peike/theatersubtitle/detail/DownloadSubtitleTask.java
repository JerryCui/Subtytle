package com.peike.theatersubtitle.detail;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ApiAsyncTask;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.api.Result;
import com.peike.theatersubtitle.api.SubtitleService;
import com.peike.theatersubtitle.cache.InternalFileCache;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class DownloadSubtitleTask extends ApiAsyncTask<String> {

    public DownloadSubtitleTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(String... params) {
        String subtitleFileId = params[0];
        SubtitleService subtitleService = getSubtitleService();
        Call<String> call = subtitleService.downloadSubtitle(subtitleFileId);

        String subText = null;
        try {
            Response<String> response = call.execute();
            if (response.isSuccess()) {
                InternalFileCache fileCache = AppApplication.getInternalFileCache();
                fileCache.writeToInternal(subtitleFileId, response.body());
            } else {
                return Result.FAIL;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }
}