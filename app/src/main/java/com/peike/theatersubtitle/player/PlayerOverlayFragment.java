package com.peike.theatersubtitle.player;


import android.os.Bundle;
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
public class PlayerOverlayFragment extends Fragment implements TimerPresenter.View {

    private View controlPanel;
    private ImageButton stopButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton resumeButton;

    private TextView timeTextView;

    private TimerPresenter timerPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_overlay, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupControlPanel(view);
        timerPresenter = new TimerPresenter(this);
    }

    private void setupControlPanel(View view) {
        controlPanel = view.findViewById(R.id.control_panel);
        timeTextView = (TextView) view.findViewById(R.id.timer_text);
        nextButton = (ImageButton) view.findViewById(R.id.next_sub_button);
        prevButton = (ImageButton) view.findViewById(R.id.previous_sub_button);
        stopButton = (ImageButton) view.findViewById(R.id.stop_player_button);
        resumeButton = (ImageButton) view.findViewById(R.id.resume_player_button);

        View.OnClickListener clickListener = new OnControlButtonClickListener();
        view.setOnClickListener(clickListener);
        nextButton.setOnClickListener(clickListener);
        prevButton.setOnClickListener(clickListener);
        stopButton.setOnClickListener(clickListener);
        resumeButton.setOnClickListener(clickListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        timerPresenter.onPause();
    }

    public void setOverlayVisibility(boolean visible) {
        controlPanel.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        timeTextView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void startTimer() {
        timerPresenter.startTime();
    }

    public void setTimer(int millisecond) {
        timerPresenter.setTimer(millisecond);
    }

    public void pauseTimer() {
        timerPresenter.onPause();
    }

    public void resumeTimer() {
        timerPresenter.resumeTimer();
    }

    @Override
    public void setTime(String time) {
        timeTextView.setText(time);
    }

    public void showResumeButton() {
        stopButton.setVisibility(View.GONE);
        resumeButton.setVisibility(View.VISIBLE);
    }

    public void showStopButton() {
        resumeButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);
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
                case R.id.resume_player_button:
                    playerActivity.onResumeClicked();
                    break;
                case R.id.player_overlay:
                    playerActivity.onOverlayClicked();
                    break;
                default:
                    break;
            }
        }
    }
}
