package com.peike.theatersubtitle.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class SubtitleRecyclerAdapter extends RecyclerView.Adapter<SubtitleRecyclerAdapter.ViewHolder> {


    public interface ClickListener {
        void onItemViewClicked(int position);
    }
    private List<Subtitle> subtitleList;
    private ClickListener listener;

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
        int flagResId = ResourceUtil.getCountryFlagResId(holder.itemView.getContext(), subtitle.getIso639());
        holder.languageImageView.setImageResource(flagResId);
        holder.downloadCountTextView.setText(String.valueOf(subtitle.getDownloadCount()));
        holder.languageTextView.setText(subtitle.getLanguage());
    }

    @Override
    public int getItemCount() {
        return subtitleList.size();
    }

    public void updateList(List<Subtitle> subtitles) {
        this.subtitleList = subtitles;
        notifyDataSetChanged();
    }

    public Subtitle getSubtitle(int position) {
        return this.subtitleList.get(position);
    }

    public void setItemClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView subFileNameTextView;
        public TextView languageTextView;
        public TextView downloadCountTextView;
        public ImageView languageImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.subFileNameTextView = (TextView) itemView.findViewById(R.id.sub_file_name);
            this.languageTextView = (TextView) itemView.findViewById(R.id.language);
            this.downloadCountTextView = (TextView) itemView.findViewById(R.id.download_count);
            this.languageImageView = (ImageView) itemView.findViewById(R.id.lang_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemViewClicked(getAdapterPosition());
                }
            });
        }
    }

}
