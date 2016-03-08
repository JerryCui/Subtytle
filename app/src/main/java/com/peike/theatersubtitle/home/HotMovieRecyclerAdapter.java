package com.peike.theatersubtitle.home;

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

public class HotMovieRecyclerAdapter
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Movie movie = movieList.get(position);
        holder.movieTitle.setText(movie.getTitle());
        holder.imdbRating.setText(MovieUtil.formatImdbRating(movie.getImdbRating()));
        holder.tomatoRating.setText(MovieUtil.formatTomatoRating(movie.getTomatoRating()));
        holder.moviePoster.setImageUrl(movie.getPosterUrl(), AppApplication.getImageLoader());
        holder.moviePlot.setText(movie.getMoviePlot());
        holder.boxOffice.setText(String.format(holder.boxOfficeUnformatted, movie.getBoxOffice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) holder.itemView.getContext()).onMovieClicked(movieList.get(position));
            }
        });
    }

    public void updateList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView moviePlot;
        NetworkImageView moviePoster;
        TextView boxOffice;
        TextView movieTitle;
        TextView imdbRating;
        TextView tomatoRating;
        final String boxOfficeUnformatted;
        public ViewHolder(View itemView) {
            super(itemView);
            this.movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            this.imdbRating = (TextView) itemView.findViewById(R.id.imdb_rating);
            this.tomatoRating = (TextView) itemView.findViewById(R.id.tomato_rating);
            this.moviePoster = (NetworkImageView) itemView.findViewById(R.id.movie_poster);
            this.boxOffice = (TextView) itemView.findViewById(R.id.box_office);
            this.moviePlot = (TextView) itemView.findViewById(R.id.movie_plot);
            boxOfficeUnformatted = itemView.getResources().getString(R.string.weekend_box_office);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}