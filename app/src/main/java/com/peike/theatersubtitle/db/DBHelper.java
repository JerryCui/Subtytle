package com.peike.theatersubtitle.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper {
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private final Context context;
    private static final String DB_NAME = "MovieSubtitle.db";

    public DBHelper(Context context) {
        this.context = context;
    }

    private DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getDaoMaster().newSession();
        }
        return daoSession;
    }

    private class MyOpenHelper extends DaoMaster.OpenHelper {
        private static final String TAG = "MyOpenHelper";

        public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Create DB-Schema (version " + Integer.toString(DaoMaster.SCHEMA_VERSION) + ")");
            super.onCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "Update DB-Schema to version: " + Integer.toString(oldVersion) + "->" + Integer.toString(newVersion));
            onCreate(db);
        }
    }
}
