package com.peike.theatersubtitle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.peike.theatersubtitle.settings.SettingsActivity;

public class BaseActivity extends AppCompatActivity {

    private ImageView closeSearchButton;
    private ImageView submitSearchButton;
    private View searchBar;
    private EditText searchBox;
    private MenuItem searchButton;


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
        searchButton = menu.findItem(R.id.action_search);
        searchBar = findViewById(R.id.search_view);
        closeSearchButton = (ImageView) findViewById(R.id.close_search);
        searchBox = (EditText) findViewById(R.id.search_edit_text);
        submitSearchButton = (ImageView) findViewById(R.id.submit_search);
        defineSearchBar();
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
            case R.id.action_search:
                showSearchEditText();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void defineSearchBar() {
        closeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                searchButton.setVisible(true);
            }
        });
    }

    private void showSearchEditText() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        searchButton.setVisible(false);
        searchBar.setVisibility(View.VISIBLE);
        searchBox.requestFocus();
    }
}
