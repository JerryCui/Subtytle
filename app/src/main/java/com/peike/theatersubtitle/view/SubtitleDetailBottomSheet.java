package com.peike.theatersubtitle.view;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.util.MovieUtil;

public class SubtitleDetailBottomSheet extends LinearLayout {

    private TextView fileTitle;
    private TextView language;
    private TextView fileSize;
    private TextView duration;
    private TextView downloadCount;
    private TextView addDate;
    private BottomSheetBehavior bottomSheetBehavior;

    public SubtitleDetailBottomSheet(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubtitleDetailBottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initView() {
        fileTitle = (TextView) findViewById(R.id.sub_file_name);
        language = (TextView) findViewById(R.id.sub_language);
        fileSize = (TextView) findViewById(R.id.file_size);
        duration = (TextView) findViewById(R.id.duration);
        downloadCount = (TextView) findViewById(R.id.download_count);
        addDate = (TextView) findViewById(R.id.add_date);
        bottomSheetBehavior = BottomSheetBehavior.from(this);
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
        language.setText(subtitle.getLanguage());
        fileSize.setText(MovieUtil.byteToKB(subtitle.getFileSize()));
        downloadCount.setText(MovieUtil.formatNumber(subtitle.getDownloadCount()));
        addDate.setText(subtitle.getAddDate());
        duration.setText(subtitle.getDuration());
    }
}
