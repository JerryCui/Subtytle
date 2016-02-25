package com.peike.theatersubtitle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.settings.SettingsActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    /**
     * Called when activity needs header to show
     *
     * @return Toolbar
     */

    protected Toolbar getToolBar(boolean hasBackButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
        }
        return toolbar;
    }

    protected Toolbar getToolBar() {
        return getToolBar(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_setting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
