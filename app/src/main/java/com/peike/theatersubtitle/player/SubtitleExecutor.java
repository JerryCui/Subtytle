package com.peike.theatersubtitle.player;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.DateTimeUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SubtitleExecutor extends Thread {

    private static final String TAG = SubtitleExecutor.class.getSimpleName();
    private final Object lock = new Object();
    private final PlayerFragment.SubtitlePresentHandler subtitlePresentHandler;
    private final String subFileName;

    boolean stopFlag = false;

    private SRTItem upcomingNode;
    private SRTItem glanceNode;
    private boolean glanceMode = false;
    private long startTime;

    SubtitleExecutor(String subFileName, PlayerFragment.SubtitlePresentHandler subtitlePresentHandler) {
        this.subFileName = subFileName;
        this.subtitlePresentHandler = subtitlePresentHandler;
    }

    @Override
    public void run() {
        List<SRTItem> subDataObjList = parseSubtitleFile(subFileName);
        Log.d(TAG, "SRT Item number: " + subDataObjList.size());
        if (subDataObjList.isEmpty()) {
            sendMessage("Invalid file.");
            return;
        }

        startTime = System.currentTimeMillis();
        startPlaying(startTime);
        upcomingNode = subDataObjList.get(0);

        try {
            long endTime;
            iteration:
            do {
                do {
                    Thread.sleep(100);
                    if (stopFlag) break iteration;
                    endTime = System.currentTimeMillis();
                } while (endTime - startTime < upcomingNode.startTimeMilli);
                synchronized (lock) {
                    if (!glanceMode)
                        sendMessage(upcomingNode);
                    upcomingNode = upcomingNode.next;
                }
            } while (upcomingNode != null && !stopFlag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void playFromGlance() {
        synchronized (lock) {
            upcomingNode = glanceNode;
            startTime = System.currentTimeMillis() - upcomingNode.startTimeMilli;
            resetTimer(startTime);
            deactivateGlance();
        }
    }

    public synchronized void resumePlayer() {
        deactivateGlance();
        if (upcomingNode.previous != null) {
            sendMessage(upcomingNode.previous);
        } else {
            sendMessage("");
        }
    }

    public synchronized void next() {
        activateGlance();
        glanceNode = glanceNode == null ? upcomingNode : glanceNode.next;
        if (glanceNode != null) { // not last node
            sendMessage(glanceNode);
            setTimer(glanceNode.startTimeMilli);
        }
    }

    public synchronized void previous() {
        activateGlance();
        if (glanceNode != null && glanceNode.previous != null) {
            glanceNode = glanceNode.previous;
            sendMessage(glanceNode);
            setTimer(glanceNode.startTimeMilli);
        }
    }

    private void activateGlance() {
        glanceMode = true;
        glanceNode = getGlanceNode();
    }

    private void deactivateGlance() {
        glanceMode = false;
        glanceNode = null;
    }

    @Nullable
    private SRTItem getGlanceNode() {
        if (glanceNode == null) {
            glanceNode = upcomingNode.previous;
        }
        return glanceNode;
    }

    private void startPlaying(long startTime) {
        Message msg = subtitlePresentHandler.obtainMessage(Constants.MSG_START_PLAYING, startTime);
        subtitlePresentHandler.sendMessage(msg);
    }

    private void sendMessage(String msgValue) {
        Message msg = subtitlePresentHandler.obtainMessage(Constants.MSG_SRT_TEXT, msgValue);
        subtitlePresentHandler.sendMessage(msg);
    }

    private void sendMessage(SRTItem srtItem) {
        Message msg = subtitlePresentHandler.obtainMessage(Constants.MSG_SRT_TEXT, srtItem.text +
                "<br/>" + DateTimeUtil.millisToHourMinuteSecondMillis(srtItem.startTimeMilli));
        subtitlePresentHandler.sendMessage(msg);
    }

    private void setTimer(int millisecond) {
        Message msg = subtitlePresentHandler.obtainMessage(Constants.MSG_SET_TIMER, (long) millisecond);
        subtitlePresentHandler.sendMessage(msg);
    }

    private void resetTimer(long startTime) {
        Message msg = subtitlePresentHandler.obtainMessage(Constants.MSG_RESET_TIMER, startTime);
        subtitlePresentHandler.sendMessage(msg);
    }

    @NonNull
    private List<SRTItem> parseSubtitleFile(String fileName) {
        List<SRTItem> result = new ArrayList<>();
        try {
            FileInputStream fis = AppApplication.getInternalFileCache().readStreamFromInternal(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            Log.d(TAG, "Open file to read: " + fileName);
            String line;
            String prevLine = "";
            SRTItem srtItem = null;
            while ((line = br.readLine()) != null) {
                if (isItemNumber(line, prevLine)) {
                    if (srtItem != null) {
                        srtItem.next = new SRTItem();
                        srtItem.next.previous = srtItem;
                        result.add(srtItem);
                        srtItem = srtItem.next;
                    } else {
                        srtItem = new SRTItem();
                    }
                    srtItem.number = Integer.valueOf(line);
                } else if (isTimeCode(line) && srtItem != null) {
                    int splitterIdx = line.indexOf("-->");
                    String startTimecode = line.substring(0, splitterIdx).trim();
                    String endTimecode = line.substring(splitterIdx + 3).trim();
                    srtItem.startTimeMilli = DateTimeUtil.timeToMillisecond(startTimecode);
                    srtItem.endTimeMilli = DateTimeUtil.timeToMillisecond(endTimecode);
                } else if (srtItem != null) {
                    srtItem.text = srtItem.text == null ? line : srtItem.text + '\n' + line;
                }
                prevLine = line;
            }
            if (srtItem != null)
                result.add(srtItem);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static boolean isItemNumber(String line, String prevLine) {
        if (line == null || line.isEmpty()) return false;
        for (char c : line.toCharArray()) {
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return "".equals(prevLine);
    }

    private static boolean isTimeCode(String line) {
        return !(line == null || line.isEmpty()) &&
                line.matches("\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}");
    }

    static class SRTItem {
        public SRTItem next;
        public SRTItem previous;

        public int number;
        public int startTimeMilli;
        public int endTimeMilli;
        public String text;
    }

}
