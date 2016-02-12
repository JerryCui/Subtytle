package com.peike.theatersubtitle.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.Movie;

import java.util.List;

public class HomeActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener
        {


    public interface HotMovieView {
        void setShowLoadingView(boolean show);

        void showEmptyText(CharSequence emptyText);

        void setMovieData(List<Movie> movieList);

        void hideRefresh();
    }

    private static final String TAG = HomeActivity.class.getSimpleName();

    private HotMovieView hotMovieView;
    private MovieDataHelper movieDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getToolBar();

        movieDataHelper = new MovieDataHelper();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof HotMovieFrament) {
            hotMovieView = (HotMovieView) fragment;
        }
    }

    /**
     * Listener of {@link HotMovieFrament} Swipe Refresh
     */
    @Override
    public void onRefresh() {
        Log.d(TAG, "OnRefresh()");
        movieDataHelper.initGetHotMovieTast(new GetHotMovieResponseListener());
    }

    private class GetHotMovieResponseListener implements ResponseListener {

        @Override
        public void onSuccess() {
            hotMovieView.setMovieData(movieDataHelper.getHotMovieList());
            hotMovieView.hideRefresh();
        }

        @Override
        public void onFailure() {

        }
    }
}
