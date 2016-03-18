package com.peike.theatersubtitle.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.MovieSearchResult;

import java.util.List;

public class SearchResultAdapter extends BaseAdapter {
    List<MovieSearchResult> results;
    @Override
    public int getCount() {
        return results == null ? 0 : results.size();
    }

    @Override
    public Object getItem(int position) {
        return results == null ? null : results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (results != null && !results.isEmpty()) {
            ViewHolder holder;
            if (convertView == null) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) v.findViewById(R.id.movie_title);
                holder.imdbRating = (TextView) v.findViewById(R.id.imdb_rating);
                holder.tomatoRating = (TextView) v.findViewById(R.id.tomato_rating);
                holder.poster = (NetworkImageView) v.findViewById(R.id.movie_poster);
                v.setTag(holder);
                convertView = v;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MovieSearchResult m = results.get(position);
            holder.title.setText(m.getTitle());
            holder.imdbRating.setText(m.getImdbRating());
            holder.tomatoRating.setText(m.getTomatoRating());
            holder.poster.setImageUrl(m.getPosterUrl(), AppApplication.getImageLoader());
        }
        return convertView;
    }

    public void updateList(List<MovieSearchResult> movieSearchResults) {
        results = movieSearchResults;
        notifyDataSetChanged();
    }

    class ViewHolder {
        public NetworkImageView poster;
        public TextView title;
        public TextView imdbRating;
        public TextView tomatoRating;
    }


}
