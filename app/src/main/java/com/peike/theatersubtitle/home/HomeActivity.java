package com.peike.theatersubtitle.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.DaoHelper;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.detail.DetailActivity;
import com.peike.theatersubtitle.util.Constants;

import java.util.List;

public class HomeActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    public interface HotMovieView {

        void showEmptyText(CharSequence emptyText);

        void setMovieData(List<Movie> movieList);

        void setRefreshing(boolean canShowRefresh);
    }

    public interface LocalMovieView {
        void setLocalMovie(List<Movie> movieList);
    }

    private static final String TAG = HomeActivity.class.getSimpleName();

    private HotMovieView hotMovieView;
    private LocalMovieView localMovieView;
    private DaoHelper daoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getToolBar(HAS_MENU_ITEM);

        daoHelper = new DaoHelper();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof HotMovieFrament) {
            hotMovieView = (HotMovieView) fragment;
        } else if (fragment instanceof LocalMovieFragment) {
            localMovieView = (LocalMovieView) fragment;
        }
    }

    public void onLocalMovieFragementStart() {
        List<Movie> localMovies = daoHelper.getLocalMovies();
        localMovieView.setLocalMovie(localMovies);
    }

    public void onHotMovieFragmentStart() {
        if (!loadCachedData()) {
            initGetHotMovieTask();
        }
    }

    private boolean loadCachedData() {
        List<Movie> movieList = daoHelper.getHotMovieList();
        if (movieList == null || movieList.isEmpty()) {
            return false;
        } else {
            hotMovieView.setMovieData(movieList);
            return true;
        }
    }

    /**
     * Listener of {@link HotMovieFrament} Swipe Refresh
     */
    @Override
    public void onRefresh() {
        Log.d(TAG, "OnRefresh()");
        initGetHotMovieTask();
    }

    private void initGetHotMovieTask() {
        new GetHotMovieTask(new GetHotMovieResponseListener()).execute();
    }

    private class GetHotMovieResponseListener implements ResponseListener {

        @Override
        public void onSuccess() {
            hotMovieView.setMovieData(daoHelper.getHotMovieList());
            hotMovieView.setRefreshing(false);
        }

        @Override
        public void onFailure() {
            hotMovieView.setRefreshing(false);
        }

    }

    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_IMDB_ID, movie.getImdbId());
        startActivity(intent);
    }
}
