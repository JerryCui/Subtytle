package com.peike.theatersubtitle.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;

public class HomeActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    public interface HotMovieView {
        void setShowLoadingView(boolean show);

        void showEmptyText(CharSequence emptyText);

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
        new GetHotMovieTask().execute();
        hotMovieView.hideRefresh();
    }
}
