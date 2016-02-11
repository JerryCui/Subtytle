package com.peike.theatersubtitle.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.DaoSession;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.db.MovieDao;

import java.util.List;

public class HomeActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    public interface HotMovieView {
        void setShowLoadingView(boolean show);

        void showEmptyText(CharSequence emptyText);

        void setMovieData(List<Movie> movieList);

        void hideRefresh();
    }

    private static final String TAG = HomeActivity.class.getSimpleName();

    private HotMovieView hotMovieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof HotMovieFrament) {
            hotMovieView = (HotMovieView) fragment;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Listener of {@link HotMovieFrament} Swipe Refresh
     */
    @Override
    public void onRefresh() {
        Log.d(TAG, "OnRefresh()");
        new GetHotMovieTask(new GetHotMovieResponseListener()).execute();
        hotMovieView.hideRefresh();
    }

    private class GetHotMovieResponseListener implements ResponseListener {

        @Override
        public void onSuccess() {
            //TODO get hot movie from db
            DaoSession daoSession = AppApplication.getDaoSession();
            MovieDao movieDao = daoSession.getMovieDao();
            List<Movie> movieList = movieDao.queryBuilder().list();
            hotMovieView.setMovieData(movieList);
            hotMovieView.hideRefresh();
        }

        @Override
        public void onFailure() {

        }
    }
}
