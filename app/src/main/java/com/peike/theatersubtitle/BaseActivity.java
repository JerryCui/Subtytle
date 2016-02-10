package com.peike.theatersubtitle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolBar();
    }

    protected Toolbar getToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (toolbar!=null) {
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }
}
