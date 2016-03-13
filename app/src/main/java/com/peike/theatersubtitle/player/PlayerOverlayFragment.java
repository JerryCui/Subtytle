package com.peike.theatersubtitle.player;


import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.peike.theatersubtitle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerOverlayFragment extends Fragment {

    private ImageButton stopButton;
    private View controlPanel;
    private ImageButton nextButton;
    private ImageButton prevButton;

    private TextView timeTextView;
    private long startTime;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new TimerRunnable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_overlay, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupControlPanel(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Not trigger when clicking on FULLSCREEN mode
                ((PlayerActivity) getActivity()).onOverlayClicked();
            }
        });
    }

    private void setupControlPanel(View view) {
        controlPanel = view.findViewById(R.id.control_panel);
        timeTextView = (TextView) view.findViewById(R.id.timer_text);
        nextButton = (ImageButton) view.findViewById(R.id.next_sub_button);
        prevButton = (ImageButton) view.findViewById(R.id.previous_sub_button);
        stopButton = (ImageButton) view.findViewById(R.id.stop_player_button);

        View.OnClickListener clickListener = new OnControlButtonClickListener();
        nextButton.setOnClickListener(clickListener);
        prevButton.setOnClickListener(clickListener);
        stopButton.setOnClickListener(clickListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void setOverlayVisibility(boolean visible) {
        controlPanel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void startTimer() {
        startTime = SystemClock.uptimeMillis();
        timerHandler.post(timerRunnable);
    }

    public void setTimer(int millisecond) {
        startTime = SystemClock.uptimeMillis() - millisecond;
    }

    class OnControlButtonClickListener implements View.OnClickListener {
        PlayerActivity playerActivity = (PlayerActivity) getActivity();

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.next_sub_button:
                    playerActivity.onNextClicked();
                    break;
                case R.id.previous_sub_button:
                    playerActivity.onPrevClicked();
                    break;
                case R.id.stop_player_button:
                    playerActivity.onStopClicked();
                    break;
                default:
                    break;
            }
        }
    }

    class TimerRunnable implements Runnable {

        @Override
        public void run() {
            long millis = SystemClock.uptimeMillis() - startTime;
            timeTextView.setText(millisToTime(millis));
            timerHandler.postDelayed(this, 500);
        }

        private String millisToTime(long millisecond) {
            int seconds = (int) (millisecond / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            return String.format("%d:%02d", minutes, seconds);
        }
    }
}
