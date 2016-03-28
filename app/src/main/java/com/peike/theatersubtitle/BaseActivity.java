package com.peike.theatersubtitle;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.peike.theatersubtitle.home.HomeActivity;
import com.peike.theatersubtitle.search.SearchActivity;
import com.peike.theatersubtitle.settings.SettingsActivity;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.KeyboardUtil;
import com.peike.theatersubtitle.view.SearchBox;


public abstract class BaseActivity extends AppCompatActivity {

    protected static final int HAS_BACK_BUTTON = 1; // settings
    protected static final int HAS_MENU_ITEM = 2; // home
    protected static final int PINNED_SEARCH_BOX = 4; // search

    private MenuItem searchButton;
    private MenuItem settingButton;
    private SearchBox searchBox;
    private View overlay;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private SearchBoxBehaviorListener searchBoxBehaviorListener;
    private boolean hasMenuItem;

    protected abstract boolean canShowBackButton();

    protected Toolbar getToolBar() {
        return getToolBar(HAS_BACK_BUTTON | HAS_MENU_ITEM);
    }

    protected Toolbar getToolBar(int flag) {
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        switch (flag) {
            case HAS_BACK_BUTTON:
                setBackButtonVisible(true);
                this.hasMenuItem = false;
                break;
            case HAS_MENU_ITEM:
                setBackButtonVisible(false);
                this.hasMenuItem = true;
                setSearchBox(toolbar);
                setModal();
                break;
            case HAS_BACK_BUTTON | HAS_MENU_ITEM:
                setBackButtonVisible(true);
                this.hasMenuItem = true;
                setSearchBox(toolbar);
                setModal();
                break;
            case PINNED_SEARCH_BOX:
                setBackButtonVisible(false);
                this.hasMenuItem = false;
                setSearchBox(toolbar);
                searchBox.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return toolbar;
    }

    private void setSearchBox(Toolbar toolbar) {
        searchBox = (SearchBox) toolbar.findViewById(R.id.search_box);

        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearchActivity(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchBox.setLeftIconResource(R.drawable.ic_arrow_back_black_24dp);
        searchBox.setOnLeftIconClickedListener(getLeftIconClickListener());
    }

    private void setModal() {
        overlay = findViewById(R.id.overlay);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseSearchBox();
            }
        });
    }

    private void startSearchActivity(String query) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constants.EXTRA_MOVIE_QUERY, query);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (hasMenuItem) {
            getMenuInflater().inflate(R.menu.menu, menu);
            searchButton = menu.findItem(R.id.action_search);
            settingButton = menu.findItem(R.id.action_setting);
        }
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
                expandSearchBox();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void expandSearchBox() {
        setSearchBoxVisible(true);
        setMenuItemVisible(false);
        setBackButtonVisible(false);
        toolbar.setContentInsetsAbsolute(0, 0);
        if (searchBoxBehaviorListener != null) {
            searchBoxBehaviorListener.onExpand();
        }
    }

    private void collapseSearchBox() {
        setSearchBoxVisible(false);
        setMenuItemVisible(true);
        int offsetLeft = getResources().getDimensionPixelOffset(R.dimen.toolbar_inset_left);
        toolbar.setContentInsetsAbsolute(offsetLeft, 0);
        toolbar.setContentInsetsRelative(offsetLeft, 0);
        if (canShowBackButton()) {
            setBackButtonVisible(true);
        }
        if (searchBoxBehaviorListener != null) {
            searchBoxBehaviorListener.onCollapse();
        }
    }


    private void setSearchBoxVisible(boolean visible) {
        searchBox.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) {
            searchBox.requestFocus();
            overlay.setVisibility(View.VISIBLE);
            KeyboardUtil.toggleSoftKeyPad(searchBox);
        } else {
            overlay.setVisibility(View.GONE);
            KeyboardUtil.hideSoftKeyPad(searchBox);
        }
    }

    private void setMenuItemVisible(boolean visible) {
        searchButton.setVisible(visible);
        settingButton.setVisible(visible);
    }

    private void setBackButtonVisible(boolean visible) {
        actionBar.setDisplayHomeAsUpEnabled(visible);
    }

    protected void setSearchBoxText(String queryString) {
        searchBox.setText(queryString);
    }

    protected void setSearchBoxBehaviorListener(SearchBoxBehaviorListener searchBoxBehaviorListener) {
        this.searchBoxBehaviorListener = searchBoxBehaviorListener;
    }

    protected View.OnClickListener getLeftIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseSearchBox();
            }
        };
    }

    public interface SearchBoxBehaviorListener {
        void onExpand();

        void onCollapse();
    }
}
