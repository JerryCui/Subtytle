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

import java.util.List;


public class LocalMovieRecyclerAdapter extends RecyclerView.Adapter<LocalMovieRecyclerAdapter.ViewHolder> {

    public interface ClickListener {
        void onItemClicked(Movie movie);
    }

    private ClickListener listener;

    private List<Movie> localMovies;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_local_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = localMovies.get(position);
        holder.title.setText(movie.getTitle());
        holder.poster.setImageUrl(movie.getPosterUrl(), AppApplication.getImageLoader());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void updateGrid(List<Movie> localMovies) {
        this.localMovies = localMovies;
    }

    public void setItemClickListener(ClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView poster;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.poster = (NetworkImageView) itemView.findViewById(R.id.movie_poster);
            this.title = (TextView) itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(localMovies.get(getAdapterPosition()));
                }
            });
        }
    }
}