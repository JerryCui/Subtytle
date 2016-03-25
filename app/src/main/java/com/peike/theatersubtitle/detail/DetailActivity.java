package com.peike.theatersubtitle.detail;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.DaoHelper;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.player.PlayerActivity;
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

        void setShowButtonProgressCircle(boolean canShow);

        void fadeInPlayButton();

        void setDownloadButtonEnabled(boolean enabled);

        void markSubtitleDownloaded(Subtitle subtitle);
    }

    private DaoHelper dataHelper;

    private Movie movie;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getToolBar();

        dataHelper = new DaoHelper();

        Intent intent = getIntent();
        String selectedImdbId = intent.getStringExtra(Constants.EXTRA_IMDB_ID);
        loadSubtitle(selectedImdbId);
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

    public void onDownloadClicked(Subtitle subtitle) {
        view.setShowButtonProgressCircle(true);
        view.setDownloadButtonEnabled(false);
        new DownloadSubtitleTask(new DownloadSubtitleResponseListener(subtitle)).execute(subtitle.getFileId().toString());
    }

    public void onPlayClicked(Subtitle subtitle) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(Constants.EXTRA_SUB_ID, subtitle.getFileId());
        startActivity(intent);
    }

    private void loadSubtitle(String selectedImdbId) {
        if (!loadCachedSubtitle(selectedImdbId)) {
            initSearchSubtitleTask(selectedImdbId);
        }
    }

    private boolean loadCachedSubtitle(String selectedImdbId) {
        List<Subtitle> subtitles = dataHelper.getCachedSubtitle(selectedImdbId);
        if (subtitles.isEmpty()) {
            return false;
        }
        view.updateSubtitle(subtitles);
        return true;
    }

    private void initSearchSubtitleTask(String selectedImdbId) {
        Set<String> preferredLanguages = SettingsUtil.getLanguagePreference(this);
        String languageParam = TextUtils.join(",", preferredLanguages);
        view.setShowProgressBar(true);
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


    private class DownloadSubtitleResponseListener implements ResponseListener {
        private final Subtitle subtitle;

        private DownloadSubtitleResponseListener(Subtitle subtitle) {
            this.subtitle = subtitle;
        }

        @Override
        public void onSuccess() {
            dataHelper.markSubtitleDownloaded(subtitle);
            view.setShowButtonProgressCircle(false);
            view.setDownloadButtonEnabled(true);
            view.fadeInPlayButton();
            view.markSubtitleDownloaded(subtitle);
        }

        @Override
        public void onFailure() {
            view.setShowButtonProgressCircle(false);
        }
    }
}
