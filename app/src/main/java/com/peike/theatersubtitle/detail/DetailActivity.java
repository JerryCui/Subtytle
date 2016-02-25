package com.peike.theatersubtitle.detail;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.MovieDataHelper;
import com.peike.theatersubtitle.util.Constants;

public class DetailActivity extends BaseActivity {

    public interface View {
        void setTitle(String title);
        void setBackdrop(String backdropUrl);
    }

    private MovieDataHelper dataHelper;
    private Movie movie;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getToolBar(true);

        dataHelper = new MovieDataHelper();

        Intent intent = getIntent();
        String selectedImdbId = intent.getStringExtra(Constants.EXTRA_IMDB_ID);
        initSearchSubtitleTask(selectedImdbId);
        movie = dataHelper.getMovie(selectedImdbId);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof DetailFragment) {
            view = (DetailFragment) fragment;
        }
    }

    public void onDetailFragmentStart() {
        view.setBackdrop(movie.getBackdropUrl());
        view.setTitle(movie.getTitle());
    }

    private void initSearchSubtitleTask(String selectedImdbId) {
        //dataHelper.initSearchSubtitleTask(new SearchSubtitleResponseListener(), selectedImdbId, "");
    }

    private class SearchSubtitleResponseListener implements ResponseListener {

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure() {

        }
    }
}
