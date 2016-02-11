package com.peike.theatersubtitle;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.peike.theatersubtitle.db.DBHelper;
import com.peike.theatersubtitle.db.DaoSession;

public class AppApplication extends Application {
    private static final String TAG = "AppApplication";
    private static AppApplication singleton;
    private DBHelper dbHelper;
    public static AppApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        dbHelper = new DBHelper(this);
    }

    public static DaoSession getDaoSession() {
        return getInstance().dbHelper.getDaoSession();
    }
}
