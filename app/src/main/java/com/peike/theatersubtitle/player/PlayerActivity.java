package com.peike.theatersubtitle.player;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.DialogUtil;

import java.lang.ref.WeakReference;

public class PlayerActivity extends BaseActivity {

    private static final int INITIAL_HIDE_DELAY = 3000;
    private static final String TAG = PlayerActivity.class.getSimpleName();
    private View decorView;

    PlayerOverlayFragment overlayFragment;
    PlayerFragment playerFragment;
    Handler hideSystemuiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setupSystemUiDisplay();

        setupPlayer();
        Log.d(TAG, "onCreate()");
    }

    private void setupSystemUiDisplay() {
        decorView = getWindow().getDecorView();
        hideSystemuiHandler = new SystemUiHandler(new WeakReference<>(this));

        overlayFragment = (PlayerOverlayFragment) getSupportFragmentManager()
                .findFragmentById(R.id.player_overlay);
        syncOverlayVisibility();
    }

    private void setupPlayer() {
        playerFragment = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.player);
        String subId = getIntent().getStringExtra(Constants.EXTRA_SUB_ID);
        playerFragment.setSubtitleFileId(subId);
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
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            delayedHide(INITIAL_HIDE_DELAY);
        } else {
            hideSystemuiHandler.removeMessages(0);
        }
    }

    public void startPlaying() {
        startTimer();

    }

    public void startTimer() {
        overlayFragment.startTimer();
    }

    public void setTimer(int millisecond) {
        overlayFragment.setTimer(millisecond);
    }

    public void onNextClicked() {
        overlayFragment.pauseTimer();
        playerFragment.onNextClicked();
        overlayFragment.showResumeButton();
    }

    public void onPrevClicked() {
        overlayFragment.pauseTimer();
        playerFragment.onPrevClicked();
        overlayFragment.showResumeButton();
    }

    public void onStopClicked() {
        showQuitConfirmDialog();
    }

    public void onResumeClicked() {
        overlayFragment.resumeTimer();
        playerFragment.onResumeClicked();
        overlayFragment.showStopButton();
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

    private void delayedHide(int delayMillis) {
        hideSystemuiHandler.removeMessages(0);
        hideSystemuiHandler.sendEmptyMessageDelayed(0, delayMillis);
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

    static class SystemUiHandler extends Handler {

        private final WeakReference<PlayerActivity> activity;

        public SystemUiHandler(WeakReference<PlayerActivity> playerActivityWeakReference) {
            this.activity = playerActivityWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            PlayerActivity playerActivity = activity.get();
            if (playerActivity != null) {
                playerActivity.hideSystemUi();
            }
        }
    }
}
