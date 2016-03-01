package com.peike.theatersubtitle.detail;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.DaoHelper;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.SettingsUtil;

import java.util.List;
import java.util.Set;

public class DetailActivity extends BaseActivity {

    public interface View {
        void setShowProgressBar(boolean canShow);
        void setTitle(String title);
        void setBackdrop(String backdropUrl);
        void updateSubtitle(List<Subtitle> subtitleList);
    }

    private DaoHelper dataHelper;
    private Movie movie;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getToolBar(true);

        dataHelper = new DaoHelper();

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
        Set<String> preferedLanguages = SettingsUtil.getLanguagePreference(this);
        String languageParam = TextUtils.join(",", preferedLanguages);
        new SearchSubtitleTask(new SearchSubtitleResponseListener()).execute(selectedImdbId, languageParam);
    }

    private class SearchSubtitleResponseListener implements ResponseListener {

        @Override
        public void onSuccess() {
            view.setShowProgressBar(false);
            List<Subtitle> subtitleList = DetailActivity.this.dataHelper.getSubtitles(movie.getImdbId());
            view.updateSubtitle(subtitleList);
        }

        @Override
        public void onFailure() {
            view.setShowProgressBar(false);
        }
    }
}
