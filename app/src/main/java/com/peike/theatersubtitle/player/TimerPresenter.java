package com.peike.theatersubtitle.player;

import android.os.Handler;
import android.os.SystemClock;

import com.peike.theatersubtitle.util.DateTimeUtil;

public class TimerPresenter {

    interface View {
        void setTime(String time);
    }

    private long startTime;
    private final View view;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new TimerRunnable();

    public TimerPresenter(View view) {
        this.view = view;
    }

    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void startTime() {
        startTime = SystemClock.uptimeMillis();
        timerHandler.post(timerRunnable);
    }

    public void setTimer(int millisecond) {
        view.setTime(DateTimeUtil.millisToHourMinuteSecond(millisecond));
    }

    public void resumeTimer() {
        timerHandler.post(timerRunnable);
    }

    class TimerRunnable implements Runnable {
        @Override
        public void run() {
            long millis = SystemClock.uptimeMillis() - startTime;
            view.setTime(DateTimeUtil.millisToHourMinuteSecond((int) millis));
            timerHandler.postDelayed(this, 500);
        }
    }
}
