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
import android.widget.ImageView;
import android.widget.TextView;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Movie;
import com.peike.theatersubtitle.util.MovieUtil;

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
        mAdapter = new HotMovieRecyclerAdapter();

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        mRecyclerView.setAdapter(mAdapter);
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
    public void setMovieData(List<Movie> movieList) {
        mAdapter.updateList(movieList);
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private static class HotMovieRecyclerAdapter extends RecyclerView.Adapter<HotMovieRecyclerAdapter.ViewHolder> {

        private List<Movie> movieList;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Movie movie = movieList.get(position);
            holder.movieTitle.setText(movie.getTitle());
            holder.imdbRating.setText(MovieUtil.formatImdbRating(movie.getImdbRating()));
            holder.tomatoRating.setText(MovieUtil.formatTomatoRating(movie.getTomatoRating()));
        }

        @Override
        public int getItemCount() {
            return movieList == null ? 0 : movieList.size();
        }

        public void updateList(List<Movie> movieList) {
            this.movieList = movieList;
            notifyDataSetChanged();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView moviePoster;
            TextView rankingNumber;
            TextView movieTitle;
            TextView imdbRating;
            TextView tomatoRating;

            public ViewHolder(View itemView) {
                super(itemView);
                this.movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
                this.imdbRating = (TextView) itemView.findViewById(R.id.imdb_rating);
                this.tomatoRating = (TextView) itemView.findViewById(R.id.tomato_rating);
                this.moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
                this.rankingNumber = (TextView) itemView.findViewById(R.id.ranking_number);
            }
        }
    }
}
