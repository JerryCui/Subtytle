package com.peike.theatersubtitle.api;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peike.theatersubtitle.api.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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
        return getRetrofit(GsonConverterFactory.create()).create(MovieService.class);
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

    protected <T> T convertFromResponse(Response apiModel, Class<T> DbModel) {
        Gson gson = new Gson();
        String intermediary = gson.toJson(apiModel);
        return gson.fromJson(intermediary, DbModel);
    }

    protected <T> List<T> convertList(List<? extends Response> responseList, Class<T> DbModel) {
        Gson gson = new Gson();
        String intermediary = gson.toJson(responseList);
        Type listType = new ListParameterizedType(DbModel);
        return gson.fromJson(intermediary, listType);
    }

    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ListParameterizedType)) {
                return false;
            }
            if (o == this) {
                return true;
            }

            ListParameterizedType listParameterizedType = (ListParameterizedType) o;

            return listParameterizedType.type.equals(this.type);
        }
    }

}
