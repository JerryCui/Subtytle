package com.peike.theatersubtitle.player;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.util.Constants;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    private static final String TAG = PlayerFragment.class.getSimpleName();
    private String subFileId;
    private SubtitleExecutor subtitleExecutorThread;
    private SubtitlePresentHandler subtitlePresentHandler;
    private TextView subTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subtitlePresentHandler = new SubtitlePresentHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        subTextView = (TextView) view.findViewById(R.id.player_sub_text);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivitiCreated()");
        subtitleExecutorThread = new SubtitleExecutor(subFileId, subtitlePresentHandler);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        subtitleExecutorThread.start();
        subTextView.setText(R.string.processing);
    }

    @Override
    public void onStop() {
        super.onStop();
        subtitlePresentHandler.removeCallbacksAndMessages(null);
        subtitleExecutorThread.stopFlag = true;
        try {
            subtitleExecutorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setSubtitleFileId(String subFileId) {
        this.subFileId = subFileId;
    }

    public void onNextClicked() {
        subtitleExecutorThread.next();
    }

    public void onPrevClicked() {
        subtitleExecutorThread.previous();
    }

    public void onResumeClicked() {subtitleExecutorThread.resumePlayer();}

    private void changeText(String newText) {
        CharSequence styledText = Html.fromHtml(newText);
        subTextView.setText(styledText);
    }

    static class SubtitlePresentHandler extends Handler {
        private WeakReference<PlayerFragment> playerFragmentWeakReference;

        SubtitlePresentHandler(PlayerFragment playerFragment) {
            this.playerFragmentWeakReference = new WeakReference<>(playerFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayerFragment playerFragment = playerFragmentWeakReference.get();
            if (playerFragment != null) {
                switch (msg.what) {
                    case Constants.MSG_SRT_TEXT:
                        playerFragment.changeText((String) msg.obj);
                        break;
                    case Constants.MSG_START_PLAYING:
                        ((PlayerActivity) playerFragment.getActivity()).startPlaying();
                        playerFragment.subTextView.setText("");
                        break;
                    case Constants.MSG_SET_TIMER:
                        ((PlayerActivity) playerFragment.getActivity()).setTimer((Integer) msg.obj);
                    default:
                        break;
                }
            }
        }
    }
}
