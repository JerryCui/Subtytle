package com.peike.theatersubtitle.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.cache.InternalFileCache;
import com.peike.theatersubtitle.db.DaoHelper;
import com.peike.theatersubtitle.db.LocalSubtitle;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.player.PlayerActivity;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.DialogUtil;
import com.peike.theatersubtitle.util.SettingsUtil;

import java.util.List;
import java.util.Set;

public class DetailActivity extends BaseActivity {

    public interface View {

        void setShowProgressView(boolean canShow);

        void setTitle(String title);

        void setBackdrop(String backdropUrl);

        void updateSubtitle(List<Subtitle> subtitleList, List<LocalSubtitle> localSubtitles);

        void updateAvailableList(List<Subtitle> subtitleList);

        void setShowButtonProgressCircle(boolean canShow);

        void fadeInPlayButton();

        void setDownloadButtonEnabled(boolean enabled);

        void markSubtitleDownloaded(Subtitle subtitle);

        void showEmptyText(@StringRes int emptyTextId);

        void setShowDetailView(boolean canShow);

        void markSubtitleDeleted(Subtitle subtitle);
    }

    private DaoHelper dataHelper;
    private Movie movie;

    private View view;
    private String selectedImdbId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getToolBar();

        setSearchBoxListener();

        dataHelper = new DaoHelper();

        Intent intent = getIntent();
        selectedImdbId = intent.getStringExtra(Constants.EXTRA_IMDB_ID);
        loadSubtitle();
        movie = dataHelper.getMovie(selectedImdbId);
    }
    private void setSearchBoxListener() {
        setSearchBoxBehaviorListener(new SearchBoxBehaviorListener() {
            @Override
            public void onExpand() {
                view.setShowDetailView(false);
            }

            @Override
            public void onCollapse() {
                view.setShowDetailView(true);
            }
        });
    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof DetailFragment) {
            view = (DetailFragment) fragment;
        }
    }

    @Override
    protected boolean canShowBackButton() {
        return true;
    }

    @Override
    protected boolean isSettingButtonVisible() {
        return false;
    }

    @Override
    protected boolean isTitleVisible() {
        return false;
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

    public void onRetryClicked() {
        view.setShowProgressView(true);
        initSearchSubtitleTask();
    }

    public void onRefresh() {
        initSearchSubtitleTask();
    }

    public void onDeleteSubtitle(final Subtitle subtitle) {
        String message = String.format("Delete %s?", subtitle.getFileName());
        DialogUtil.confirm(this, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    deleteSubtitleConfirmed(subtitle);
                }
            }
        });
    }

    private void deleteSubtitleConfirmed(Subtitle subtitle) {
        dataHelper.deleteSubtitleByImdbId(subtitle.getImdbId());
        InternalFileCache fileCache = AppApplication.getInternalFileCache();
        fileCache.deleteSubtitle(String.valueOf(subtitle.getFileId()));
        view.markSubtitleDeleted(subtitle);
    }

    private void loadSubtitle() {
        if (!loadCachedSubtitle()) {
            view.setShowProgressView(true);
            initSearchSubtitleTask();
        }
    }

    private boolean loadCachedSubtitle() {
        List<Subtitle> subtitles = dataHelper.getCachedSubtitle(selectedImdbId);
        List<LocalSubtitle> localSubtitles = dataHelper.getLocalSubtitle(selectedImdbId);
        if (subtitles.isEmpty() && localSubtitles.isEmpty()) {
            return false;
        }
        view.updateSubtitle(subtitles, localSubtitles);
        return true;
    }

    private void initSearchSubtitleTask() {
        Set<String> preferredLanguages = SettingsUtil.getLanguagePreference(this);
        String languageParam = null;
        if (!preferredLanguages.isEmpty()) {
            languageParam = TextUtils.join(",", preferredLanguages);
        }
        new SearchSubtitleTask(new SearchSubtitleResponseListener()).execute(selectedImdbId, languageParam);
    }

    private class SearchSubtitleResponseListener implements ResponseListener {

        @Override
        public void onSuccess() {
            view.setShowProgressView(false);
            List<Subtitle> subtitleList = dataHelper.getCachedSubtitle(movie.getImdbId());
            if (subtitleList.isEmpty() && !dataHelper.hasLocalSubtitle()) {
                view.showEmptyText(R.string.no_subtitle_found);
            } else {
                view.updateAvailableList(subtitleList);
            }
        }
        @Override
        public void onFailure() {
            view.setShowProgressView(false);
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
