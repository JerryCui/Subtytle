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

    public interface ItemClickListener {
        void onClick(Movie movie);
    }

    private HotMovieRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;
    private LinearLayoutManager mLinearLayoutManager;
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
//        mInitText = (TextView) view.findViewById(R.id.init_text);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mEmptyText = (TextView) view.findViewById(R.id.empty_text);
        mLoadingView = view.findViewById(R.id.loading_view);
        mAdapter = new HotMovieRecyclerAdapter();
        mLinearLayoutManager = new LinearLayoutManager(getContext());

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

    private class HotMovieRecyclerAdapter
            extends RecyclerView.Adapter<HotMovieRecyclerAdapter.ViewHolder> {

        private List<Movie> movieList;

        public HotMovieRecyclerAdapter() {
            this.movieList = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Movie movie = movieList.get(position);
            holder.movieTitle.setText(movie.getTitle());
            holder.imdbRating.setText(MovieUtil.formatImdbRating(movie.getImdbRating()));
            holder.tomatoRating.setText(MovieUtil.formatTomatoRating(movie.getTomatoRating()));
            holder.moviePoster.setImageUrl(movie.getPosterUrl(), AppApplication.getImageLoader());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) getActivity()).onMovieClicked(movieList.get(position));
                }
            });
        }

        public void updateList(List<Movie> movieList) {
            this.movieList = movieList;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            NetworkImageView moviePoster;
            TextView rankingNumber;
            TextView movieTitle;
            TextView imdbRating;
            TextView tomatoRating;
            public ViewHolder(View itemView) {
                super(itemView);
                this.movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
                this.imdbRating = (TextView) itemView.findViewById(R.id.imdb_rating);
                this.tomatoRating = (TextView) itemView.findViewById(R.id.tomato_rating);
                this.moviePoster = (NetworkImageView) itemView.findViewById(R.id.movie_poster);
                this.rankingNumber = (TextView) itemView.findViewById(R.id.ranking_number);
            }

        }
        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }
}
