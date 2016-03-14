package com.peike.theatersubtitle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.peike.theatersubtitle.search.SearchActivity;
import com.peike.theatersubtitle.settings.SettingsActivity;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.KeyboardUtil;

public class BaseActivity extends AppCompatActivity {

    private MenuItem searchButton;
    private MenuItem settingButton;
    private View searchBar;
    private SearchView searchView;
    private EditText searchViewEditText;
    private ImageView searchViewLeftButton;


    /**
     * Called when activity needs header to show
     *
     * @return Toolbar
     */

    protected Toolbar getToolBar(boolean hasBackButton) {
        return getToolBar(hasBackButton, false);
    }

    protected Toolbar getToolBar(boolean hasBackButton, boolean hasMenuItem) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (toolbar != null) {
            if (hasMenuItem)
                setupSearchBar(toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
        }
        return toolbar;
    }

    protected void showSearchView(CharSequence text) {
        searchBar.setVisibility(View.VISIBLE);
        searchViewEditText.setText(text);
        searchViewEditText.clearFocus();
    }

    private void setupSearchBar(Toolbar toolbar) {
        searchBar = toolbar.findViewById(R.id.search_bar);
        searchView = (SearchView) searchBar.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearchBarVisible(false);
                startSearchActivity(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        int leftIconId = searchView.getResources().getIdentifier("android:id/search_mag_icon", null, null);
        int editTextResId = searchView.getResources().getIdentifier("android:id/search_src_text", null, null);
        if (leftIconId != 0) {
            searchViewLeftButton = (ImageView) searchView.findViewById(leftIconId);
            searchViewLeftButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            searchViewLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSearchBarVisible(false);
                }
            });
        }
        if (editTextResId != 0) {
            searchViewEditText = (EditText) searchView.findViewById(editTextResId);
        }
    }

    private void startSearchActivity(String query) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constants.EXTRA_MOVIE_QUERY, query);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchButton = menu.findItem(R.id.action_search);
        settingButton = menu.findItem(R.id.action_setting);
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
                setSearchBarVisible(true);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setSearchViewLeftButtonOnClickListener(View.OnClickListener listener) {
        searchViewLeftButton.setOnClickListener(listener);
    }

    private void setSearchBarVisible(boolean visible) {
        searchButton.setVisible(!visible);
        settingButton.setVisible(!visible);
        searchBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) {
            searchView.requestFocus();
            KeyboardUtil.toggleSoftKeyPad(searchView);
        } else {
            KeyboardUtil.hideSoftKeyPad(searchView);
        }
    }
}
