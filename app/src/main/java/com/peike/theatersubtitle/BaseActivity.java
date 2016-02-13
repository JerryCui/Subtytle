package com.peike.theatersubtitle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.peike.theatersubtitle.db.DaoSession;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    /**
     * Called when activity needs header to show
     * @return Toolbar
     */
    protected Toolbar getToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (toolbar!=null) {
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_refresh) {
//            DaoSession daoSession = AppApplication.getDaoSession();
//            daoSession.getMovieDao().deleteAll();
//            daoSession.getSubtitleDao().deleteAll();
//            Toast.makeText(this, "database cleared", Toast.LENGTH_LONG).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
