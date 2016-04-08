package com.peike.theatersubtitle.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.LocalSubtitle;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.util.ResourceUtil;
import com.peike.theatersubtitle.view.SubtitleIcon;

import java.util.List;

public class SubtitleRecyclerAdapter extends RecyclerView.Adapter<SubtitleRecyclerAdapter.ViewHolder> {

    private static final int VIEWTYPE_SUBHEADER_DOWNLOADED = 1;
    private static final int VIEWTYPE_SUBHEADER_NOT_DOWNLOADED = 2;
    private static final int VIEWTYPE_LOCAL_SUBTITLE = 4;
    private static final int VIEWTYPE_AVAILABLE_SUBTITLE = 8;

    public interface ClickListener {

        void onItemViewClicked(int subtitleIdx);

        void onItemDeleteClicked(int subtitleIdx);

    }

    private MixedSubtitle mixedSubtitle;

    private ClickListener listener;

    public SubtitleRecyclerAdapter() {
        mixedSubtitle = new MixedSubtitle();
    }

    @Override
    public int getItemViewType(int position) {
        if (mixedSubtitle.hasLocalSubtitle() && position < mixedSubtitle.getLocalSubtitleCount()) {
            return VIEWTYPE_LOCAL_SUBTITLE;
        } else if (position == mixedSubtitle.getLocalSubtitleCount()) {
            return VIEWTYPE_SUBHEADER_DOWNLOADED;
        } else {
            return VIEWTYPE_AVAILABLE_SUBTITLE;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEWTYPE_SUBHEADER_NOT_DOWNLOADED:
            case VIEWTYPE_SUBHEADER_DOWNLOADED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.subheader, parent, false);
                break;
            case VIEWTYPE_LOCAL_SUBTITLE:
            case VIEWTYPE_AVAILABLE_SUBTITLE:
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_subtitle, parent, false);
                break;
        }
        return new ViewHolder(view, viewType, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEWTYPE_LOCAL_SUBTITLE:
                setupSubtitleItemView(holder, position, true);
                break;
            case VIEWTYPE_AVAILABLE_SUBTITLE:
                setupSubtitleItemView(holder, position - 1, false);
            default:
                break;
        }

    }

    private void setupSubtitleItemView(ViewHolder holder, int position, boolean isLocal) {
        Subtitle subtitle = mixedSubtitle.getSubtitle(position);
        holder.subFileNameTextView.setText(subtitle.getFileName());
        int flagResId = ResourceUtil.getCountryFlagResId(holder.itemView.getContext(), subtitle.getIso639());
        holder.languageImageView.setImageResource(flagResId);
        holder.downloadCountTextView.setText(String.valueOf(subtitle.getDownloadCount()));
        holder.languageTextView.setText(subtitle.getLanguage());
        if (isLocal) {
            holder.deleteIcon.setVisibility(View.VISIBLE);
            holder.availableIcon.markDownloaded();
        } else {
            holder.deleteIcon.setVisibility(View.GONE);
            holder.availableIcon.setImageResource(R.drawable.ic_cloud_circle_white_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return mixedSubtitle.getTotalCount() + 1;
    }

    public void updateList(List<Subtitle> subtitles, List<LocalSubtitle> localSubtitles) {
        mixedSubtitle.updateMixedSubtitle(subtitles, localSubtitles);
        notifyDataSetChanged();
    }

    public void updateAvailableList(List<Subtitle> subtitleList) {
        mixedSubtitle.updateSubtitle(subtitleList);
        notifyDataSetChanged();
    }

    public void setItemClickListener(ClickListener listener) {
        this.listener = listener;
    }

    /**
     * @param position the index, could be of subhead
     * @return Subtitle clicked
     */
    public Subtitle getSubtitle(int position) {
        if (mixedSubtitle.hasLocalSubtitle()) {
            if (position == mixedSubtitle.getLocalSubtitleCount()) {
                return null;
            } else if (position > mixedSubtitle.getLocalSubtitleCount()) {
                position--;
            }
        } else {
            position--;
        }
        return mixedSubtitle.getSubtitle(position);
    }

    public void markSubtitleDownloaded(Subtitle subtitle) {
        int index = mixedSubtitle.markDownloaded(subtitle) + 1;
        notifyItemMoved(index, mixedSubtitle.getLocalSubtitleCount() - 1);
    }

    public void markSubtitleDeleted(Subtitle subtitle) {
        int index = mixedSubtitle.markDeleted(subtitle);
        notifyItemMoved(index, getItemCount()-1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView subFileNameTextView;
        public TextView languageTextView;
        public TextView downloadCountTextView;
        public ImageView languageImageView;
        public SubtitleIcon availableIcon;
        public SubtitleIcon deleteIcon;

        public ViewHolder(View itemView, int viewType, final ClickListener listener) {
            super(itemView);
            if (viewType != VIEWTYPE_SUBHEADER_DOWNLOADED && viewType != VIEWTYPE_SUBHEADER_NOT_DOWNLOADED) {
                this.subFileNameTextView = (TextView) itemView.findViewById(R.id.sub_file_name);
                this.languageTextView = (TextView) itemView.findViewById(R.id.language);
                this.downloadCountTextView = (TextView) itemView.findViewById(R.id.download_count);
                this.languageImageView = (ImageView) itemView.findViewById(R.id.lang_img);
                this.availableIcon = (SubtitleIcon) itemView.findViewById(R.id.available_at);
                this.deleteIcon = (SubtitleIcon) itemView.findViewById(R.id.delete_button);
                this.deleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemDeleteClicked(getAdapterPosition());
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemViewClicked(getAdapterPosition());
                    }
                });
            }
        }
    }
}
