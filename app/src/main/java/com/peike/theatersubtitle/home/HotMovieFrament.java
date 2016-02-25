package com.peike.theatersubtitle.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.util.MovieUtil;

import java.util.ArrayList;
import java.util.List;

public class HotMovieFrament extends Fragment implements HomeActivity.HotMovieView {

    private HotMovieRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;
    private TextView mEmptyText;
    private View mLoadingView;
    private RecyclerView mRecyclerView;
    private TextView mInitText;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof HomeActivity) {
            this.mRefreshListener = (SwipeRefreshLayout.OnRefreshListener) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HomeActivity)getActivity()).onHotMovieFragmentStart();
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mEmptyText = (TextView) view.findViewById(R.id.empty_text);
        mLoadingView = view.findViewById(R.id.loading_view);
        mAdapter = new HotMovieRecyclerAdapter();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

    }


    @Override
    public void setShowLoadingView(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmptyText(CharSequence emptyText) {
        mEmptyText.setText(emptyText);
    }

    @Override
    public void setMovieData(List<Movie> movieList) {
        mAdapter.updateList(movieList);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }
}
