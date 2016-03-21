package com.peike.theatersubtitle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.peike.theatersubtitle.R;

public class SubtitleIcon extends ImageView {
    public SubtitleIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SubtitleIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a  = context.obtainStyledAttributes(attrs, R.styleable.SubtitleIcon, 0, 0);
        int iconBackgroundColor = a.getColor(R.styleable.SubtitleIcon_backgroundColor, Color.BLUE);
        setColorFilter(iconBackgroundColor, PorterDuff.Mode.SRC_IN);
        a.recycle();
    }

    public void markDownloaded() {
        setImageResource(R.drawable.ic_check_circle_white_48dp);
    }
}
