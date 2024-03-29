package com.peike.theatersubtitle.view;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.util.MovieUtil;

public class SubtitleDetailBottomSheet extends LinearLayout {

    private TextView fileTitlePeek;
    private TextView fileTitle;
    private TextView language;
    private TextView fileSize;
    private TextView duration;
    private TextView downloadCount;
    private TextView addDate;
    private BottomSheetBehavior bottomSheetBehavior;
    private View fullDetailView;

    public SubtitleDetailBottomSheet(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubtitleDetailBottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_subtitle_detail, this);
        setOrientation(VERTICAL);
    }


    public void initView() {
        fullDetailView = findViewById(R.id.sub_file_full_detail);
        fileTitlePeek = (TextView) findViewById(R.id.sub_file_peek_text);
        fileTitle = (TextView) findViewById(R.id.sub_file_name);
        language = (TextView) findViewById(R.id.sub_language);
        fileSize = (TextView) findViewById(R.id.file_size);
        duration = (TextView) findViewById(R.id.duration);
        downloadCount = (TextView) findViewById(R.id.download_count);
        addDate = (TextView) findViewById(R.id.add_date);
        bottomSheetBehavior = BottomSheetBehavior.from(this);
        fullDetailView.setVisibility(INVISIBLE);
    }

    public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback callback) {
        bottomSheetBehavior.setBottomSheetCallback(callback);
    }

    public void setState(@BottomSheetBehavior.State int state) {
        bottomSheetBehavior.setState(state);
    }

    @BottomSheetBehavior.State
    public int getState() {
        return bottomSheetBehavior.getState();
    }

    public void updateDetail(Subtitle subtitle) {
        fileTitle.setText(subtitle.getFileName());
        fileTitlePeek.setText(subtitle.getFileName());
        language.setText(subtitle.getLanguage());
        fileSize.setText(MovieUtil.byteToKB(subtitle.getFileSize()));
        downloadCount.setText(MovieUtil.formatNumber(subtitle.getDownloadCount()));
        addDate.setText(subtitle.getAddDate());
        duration.setText(subtitle.getDuration());
    }

    public void expand() {
        fullDetailView.setVisibility(VISIBLE);
        setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void collapse() {
        setState(BottomSheetBehavior.STATE_COLLAPSED);
        fullDetailView.setVisibility(INVISIBLE);
    }

    public void showDetailView() {
        fullDetailView.setVisibility(VISIBLE);
    }

    public void hideDetailView() {
        fullDetailView.setVisibility(INVISIBLE);
    }

}
