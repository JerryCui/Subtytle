package com.peike.theatersubtitle.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;

import java.util.ArrayList;
import java.util.List;

public class SubtitleRecyclerAdapter extends RecyclerView.Adapter<SubtitleRecyclerAdapter.ViewHolder> {

    private List<Subtitle> subtitleList;

    public SubtitleRecyclerAdapter() {
        subtitleList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subtitle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subtitle subtitle = subtitleList.get(position);
        holder.subFileNameTextView.setText(subtitle.getFileName());
    }

    @Override
    public int getItemCount() {
        return subtitleList.size();
    }

    public void updateList(List<Subtitle> subtitles) {
        this.subtitleList = subtitles;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView subFileNameTextView;
        public TextView languageTextView;
        public TextView downloadCountTextView;
        public ImageView languageImageView;
        public ImageView downloadIconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.subFileNameTextView = (TextView) itemView.findViewById(R.id.sub_file_name);
//            this.languageTextView = (TextView) itemView.findViewById(R.id.language);
//            this.downloadCountTextView = (TextView) itemView.findViewById(R.id.download_count);
//            this.languageImageView = (ImageView) itemView.findViewById(R.id.lang_img);
//            this.downloadIconImageView = (ImageView) itemView.findViewById(R.id.download_icon);
        }
    }

}
