package com.peike.theatersubtitle.api;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiAsyncTask<Param>
        extends AsyncTask<Param, Void, Result> {
    protected static final String URL = "https://aqueous-falls-1653.herokuapp.com";

    protected final ResponseListener responseListener;

    public ApiAsyncTask(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    protected MovieService getMovieService() {
        return getMovieService(GsonConverterFactory.create());
    }

    protected MovieService getMovieService(Converter.Factory converterFactory) {
        return getRetrofit(converterFactory).create(MovieService.class);
    }

    protected SubtitleService getSubtitleService() {
        return getSubtitleService(GsonConverterFactory.create());
    }

    protected SubtitleService getSubtitleService(Converter.Factory converterFactory) {
        return getRetrofit(converterFactory).create(SubtitleService.class);
    }

    private Retrofit getRetrofit(Converter.Factory converterFactory) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build();
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
