package com.peike.theatersubtitle;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.peike.theatersubtitle.cache.LruBitmapCache;
import com.peike.theatersubtitle.db.DBHelper;
import com.peike.theatersubtitle.db.DaoSession;

public class AppApplication extends Application {
    private static final String TAG = "AppApplication";
    private static final int CACHE_SIZE_BYTE = 4 * 1024 * 1024;
    private static AppApplication singleton;
    private DBHelper dbHelper;
    private ImageLoader imageLoader;
    public static AppApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        dbHelper = new DBHelper(this);
        imageLoader = new ImageLoader(Volley.newRequestQueue(this), new LruBitmapCache(CACHE_SIZE_BYTE));
    }

    public static DaoSession getDaoSession() {
        return getInstance().dbHelper.getDaoSession();
    }

    public static ImageLoader getImageLoader() {
        return getInstance().imageLoader;
    }
}
