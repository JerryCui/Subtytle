package com.peike.theatersubtitle.api;

import com.peike.theatersubtitle.api.model.SubtitleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SubtitleService {
    @GET("subtitle/{imdbId}")
    Call<List<SubtitleResponse>> searchSubtitle(@Path("imdbId") String imdbId, @Query("lang") String language);

    @GET("subtitle/download/{subFileId}")
    Call<String> downloadSubtitle(@Path("subFileId") String subFileId);
}
