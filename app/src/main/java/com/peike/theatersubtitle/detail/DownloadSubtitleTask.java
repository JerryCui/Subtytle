package com.peike.theatersubtitle.detail;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.api.ApiAsyncTask;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.api.Result;
import com.peike.theatersubtitle.api.SubtitleService;
import com.peike.theatersubtitle.cache.InternalFileCache;
import com.peike.theatersubtitle.db.LocalSubtitle;
import com.peike.theatersubtitle.db.LocalSubtitleDao;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.db.SubtitleDao;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DownloadSubtitleTask extends ApiAsyncTask<String> {

    public DownloadSubtitleTask(ResponseListener responseListener) {
        super(responseListener);
    }

    @Override
    protected Result doInBackground(String... params) {
        String subtitleFileId = params[0];
        SubtitleService subtitleService = getSubtitleService(ScalarsConverterFactory.create());
        Call<String> call = subtitleService.downloadSubtitle(subtitleFileId);

        try {
            Response<String> response = call.execute();
            if (response.isSuccess()) {
                persistSubtitleFile(subtitleFileId, response.body());
                persistLocalSubtitleDb(subtitleFileId);
            } else {
                return Result.FAIL;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        return Result.SUCCESS;
    }

    private void persistSubtitleFile(String subtitleFileId, String fileContent) {
        InternalFileCache fileCache = AppApplication.getInternalFileCache();
        fileCache.writeToInternal(subtitleFileId, fileContent);
    }

    private void persistLocalSubtitleDb(String subtitleFileId) {
        SubtitleDao subtitleDao = AppApplication.getSubtitleDao();
        Subtitle subtitle = subtitleDao.queryBuilder()
                .where(SubtitleDao.Properties.FileId.eq(subtitleFileId))
                .unique();
        LocalSubtitle localSubtitle = convert(subtitle, LocalSubtitle.class);
        LocalSubtitleDao localSubtitleDao = AppApplication.getLocalSubtitleDao();
        localSubtitleDao.insertOrReplace(localSubtitle);
    }
}