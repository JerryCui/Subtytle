package com.peike.theatersubtitle.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.fileName.setText(subtitle.getFileName());
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

        private TextView fileName;

        public ViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.sub_file_name);
        }
    }

}
