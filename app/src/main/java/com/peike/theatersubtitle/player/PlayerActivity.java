package com.peike.theatersubtitle.player;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.DialogUtil;

public class PlayerActivity extends BaseActivity {

    private static final String TAG = PlayerActivity.class.getSimpleName();
    private View decorView;

    PlayerOverlayFragment overlayFragment;
    PlayerFragment playerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setupSystemUiDisplay();

        setupPlayer();
    }

    private void setupSystemUiDisplay() {
        decorView = getWindow().getDecorView();

        overlayFragment = (PlayerOverlayFragment) getSupportFragmentManager()
                .findFragmentById(R.id.player_overlay);
        syncOverlayVisibility();
    }

    private void setupPlayer() {
        playerFragment = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.player);
        int subId = getIntent().getIntExtra(Constants.EXTRA_SUB_ID, 0);
        playerFragment.setSubtitleFileId(String.valueOf(subId));
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideSystemUi();
    }

    @Override
    public void onBackPressed() {
        showQuitConfirmDialog();
    }

    @Override
    protected boolean canShowBackButton() {
        return false;
    }

    public void startPlaying(long startTime) {
        overlayFragment.startTimer(startTime);
    }

    public void setTimer(long millisecond) {
        overlayFragment.setTimer(millisecond);
    }

    public void onNextClicked() {
        overlayFragment.pauseTimer();
        playerFragment.onNextClicked();
        overlayFragment.showPlayButton();
        overlayFragment.setShowResumeButton(true);
    }

    public void onPrevClicked() {
        overlayFragment.pauseTimer();
        playerFragment.onPrevClicked();
        overlayFragment.showPlayButton();
        overlayFragment.setShowResumeButton(true);
    }

    public void onStopClicked() {
        showQuitConfirmDialog();
    }

    public void onResumeClicked() {
        overlayFragment.resumeTimer();
        playerFragment.onResumeClicked();
        overlayFragment.showStopButton();
        overlayFragment.setShowResumeButton(false);
    }

    public void onPlayClicked() {
        overlayFragment.setShowResumeButton(false);
        playerFragment.onPlayClicked();
        overlayFragment.showStopButton();
    }

    public void resetTimer(long startTime) {
        overlayFragment.resetTimer(startTime);
    }

    public void onOverlayClicked() {
        hideSystemUi();
    }

    public void hideSystemUi() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            getWindow().getDecorView().setSystemUiVisibility(flag);
        }
    }

    private void syncOverlayVisibility() {
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                boolean visible = visibility == 0;
                overlayFragment.setOverlayVisibility(visible);
            }
        });
    }

    private void showQuitConfirmDialog() {
        DialogUtil.confirm(this, "Quit?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    finish();
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss();
                }
            }
        });
    }
}
