package com.peike.theatersubtitle;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.peike.theatersubtitle.cache.InternalFileCache;
import com.peike.theatersubtitle.cache.LruBitmapCache;
import com.peike.theatersubtitle.db.DBHelper;
import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.db.MovieDao;
import com.peike.theatersubtitle.db.MovieSearchResultDao;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.db.SubtitleDao;
import com.peike.theatersubtitle.util.SettingsUtil;

import java.util.Locale;

public class AppApplication extends Application {
    private static final String TAG = "AppApplication";
    private static final int CACHE_SIZE_BYTE = 4 * 1024 * 1024;
    private static AppApplication singleton;
    private DBHelper dbHelper;
    private InternalFileCache fileCache;
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
        fileCache = new InternalFileCache(this);
        setupDefaultLanguage();
    }

    private void setupDefaultLanguage() {
        if (!SettingsUtil.isFirstRunProcessComplete(this)) {
            String language = Locale.getDefault().getISO3Language();
            SettingsUtil.setDefaultLanguage(this, language);
            SettingsUtil.markFirstRunProcessesDone(this, false);
        }
    }

    public static DaoSession getDaoSession() {
        return getInstance().dbHelper.getDaoSession();
    }

    public static MovieDao getMovieDao() {
        return getDaoSession().getMovieDao();
    }

    public static SubtitleDao getSubtitleDao() {
        return getDaoSession().getSubtitleDao();
    }

    public static MovieSearchResultDao getMovieSearchResultDao() {
        return getDaoSession().getMovieSearchResultDao();
    }

    public static ImageLoader getImageLoader() {
        return getInstance().imageLoader;
    }

    public static InternalFileCache getInternalFileCache() { return getInstance().fileCache; }
}
