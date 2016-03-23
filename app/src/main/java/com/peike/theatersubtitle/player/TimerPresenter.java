package com.peike.theatersubtitle.player;

import android.os.Handler;

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

    public void startTimer(long startTime) {
        onPause();
        this.startTime = startTime;
        resumeTimer();
    }

    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void setTimer(long millisecond) {
        if (millisecond >= Integer.MAX_VALUE) {
            return;
        }
        view.setTime(DateTimeUtil.millisToHourMinuteSecond((int) millisecond));
    }

    public void resumeTimer() {
        timerHandler.post(timerRunnable);
    }

    class TimerRunnable implements Runnable {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            view.setTime(DateTimeUtil.millisToHourMinuteSecond((int) millis));
            timerHandler.postDelayed(this, 500);
        }
    }
}
