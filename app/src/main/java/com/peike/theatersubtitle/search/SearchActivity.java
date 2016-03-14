package com.peike.theatersubtitle.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.util.Constants;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getToolBar(false, true);


        Intent intent = getIntent();
        String queryString = intent.getStringExtra(Constants.EXTRA_MOVIE_QUERY);
        showSearchView(queryString);
        setSearchViewLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSearchMovieTask(queryString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void initSearchMovieTask(String queryString) {

    }


}
