package com.peike.theatersubtitle.api;

import android.os.AsyncTask;

import retrofit2.Response;

public abstract class ApiAsyncTask<Param, Progress, Result>
        extends AsyncTask<Param, Progress, Result> {
    protected static final String URL = "https://aqueous-falls-1653.herokuapp.com";

    protected final ResponseListener responseListener;

    public ApiAsyncTask(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }
    public void callApi() {

    }

    protected void onFail() {

    }

    protected void onSuccess() {

    }
}
