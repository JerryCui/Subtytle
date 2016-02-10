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

import com.peike.theatersubtitle.R;

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
        View view = inflater.inflate(R.layout.fragment_hot_movie, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mInitText = (TextView) view.findViewById(R.id.init_text);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mEmptyText = (TextView) view.findViewById(R.id.empty_text);
        mLoadingView = view.findViewById(R.id.loading_view);
        HotMovieRecyclerAdapter adapter = new HotMovieRecyclerAdapter();

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void hideRefresh() {

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                HotMovieFrament.this.mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }

    private static class HotMovieRecyclerAdapter extends RecyclerView.Adapter<HotMovieRecyclerAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 15;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.textView = (TextView) itemView.findViewById(R.id.movie_title);
            }
        }
    }
}
