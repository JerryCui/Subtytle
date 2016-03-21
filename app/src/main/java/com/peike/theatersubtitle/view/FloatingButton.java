package com.peike.theatersubtitle.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;

public class FloatingButton extends FrameLayout {


    private static final String TAG = FloatingButton.class.getSimpleName();
    private FloatingActionButton playButton;
    private FloatingActionButton downloadButton;
    private ProgressBar progressBar;

    public FloatingButton(Context context) {
        this(context, null);
    }

    public FloatingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_progressbar_fab, this);

        progressBar = (ProgressBar) findViewById(R.id.progress_circle);
        downloadButton = (FloatingActionButton) findViewById(R.id.download_button);
        playButton = (FloatingActionButton) findViewById(R.id.play_button);
    }

    public void showProgressCircle() {
        progressBar.setVisibility(VISIBLE);
    }

    public void hideProgressCircle() {
        progressBar.setVisibility(INVISIBLE);
    }

    public void setDownloadClickListener(OnClickListener listener) {
        downloadButton.setOnClickListener(listener);
    }

    public void setPlayButtonClickListener(OnClickListener listener) {
        playButton.setOnClickListener(listener);
    }

    public void fadeInPlayButton() {
        playButton.setAlpha(0F);
        playButton.setVisibility(VISIBLE);
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(playButton, View.ALPHA, 1F);
        fadeAnim.start();
    }

    public void showPlayButton() {
        playButton.setVisibility(VISIBLE);
    }

    public void hidePlayButton() {
        playButton.setVisibility(GONE);
    }

    public void setDownloadButtonEnabled(boolean enabled) {
        downloadButton.setEnabled(enabled);
    }

    public void setAssociatedSubtitle(Subtitle subtitle) {
        Log.d(TAG, "Download button has click listener: " + downloadButton.hasOnClickListeners());
        Log.d(TAG, "Play button has click listener: " + playButton.hasOnClickListeners());
        downloadButton.setTag(subtitle);
        playButton.setTag(subtitle);
    }
}
