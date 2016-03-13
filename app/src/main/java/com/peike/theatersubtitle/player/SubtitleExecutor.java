package com.peike.theatersubtitle.player;

import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.DateTimeUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SubtitleExecutor extends Thread {

    private static final String TAG = SubtitleExecutor.class.getSimpleName();


    String subFileName;
    SRTItem currentNode;
    boolean stopFlag = false;
    boolean gotKickOff = false;
    List<SRTItem> subDataObjList;
    private final PlayerFragment.SubtitlePresentHandler subtitlePresentHandler;

    SubtitleExecutor(String subFileName, PlayerFragment.SubtitlePresentHandler subtitlePresentHandler) {
        this.subFileName = subFileName;
        this.subtitlePresentHandler = subtitlePresentHandler;
    }

    @Override
    public void run() {
        subDataObjList = parseSubtitleFile(subFileName);
        Log.d(TAG, "SRT Item number: " + subDataObjList.size());
        if (subDataObjList == null || subDataObjList.isEmpty()) {
            sendMessage("Subtitle file is not valid.");
            return;
        }

        startTimer();

        currentNode = subDataObjList.get(0);
        try {
            long startTime = SystemClock.uptimeMillis(), endTime;
            iteration:
            do {
                gotKickOff = false;
                do {
                    if (stopFlag) break iteration;
                    Thread.sleep(100);
                    endTime = SystemClock.uptimeMillis();
                } while (endTime - startTime < currentNode.startTimeMilli && !gotKickOff);
                if (gotKickOff) {
                    setTimer(currentNode.startTimeMilli);
                }
                sendMessage(currentNode.text);
                currentNode = currentNode.next;
            } while (currentNode != null && !stopFlag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void next() {
        gotKickOff = true;
    }

    public synchronized void previous() {
        // currentNode.previous == null: first node still has not shown
        if (currentNode.previous != null) {
            currentNode = currentNode.previous;
            // currentNode.previous == null: first node is on the screen
            if (currentNode.previous != null) {
                currentNode = currentNode.previous;
            }
            gotKickOff = true; // kick off only after first node has shown
        }
    }

    private int getSleepTime() {
        int startTime = currentNode.previous == null ? 0 : currentNode.previous.startTimeMilli;
        int endTime = currentNode.startTimeMilli;
        return endTime - startTime;
    }

    private void startTimer() {
        subtitlePresentHandler.sendEmptyMessage(Constants.MSG_START_PLAYING);
    }

    private void sendMessage(String msgValue) {
        Message msg = subtitlePresentHandler.obtainMessage(Constants.MSG_SRT_TEXT, msgValue);
        subtitlePresentHandler.sendMessage(msg);
    }

    private void setTimer(int millisecond) {
        Message msg = subtitlePresentHandler.obtainMessage(Constants.MSG_SET_TIMER, millisecond);
        subtitlePresentHandler.sendMessage(msg);
    }

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
                    srtItem.startTimeMilli = DateTimeUtil.timecodeToMillisecond(startTimecode);
                    srtItem.endTimeMilli = DateTimeUtil.timecodeToMillisecond(endTimecode);
                } else if (srtItem != null) {
                    srtItem.text = srtItem.text == null ? line : srtItem.text + '\n' + line;
                }
                prevLine = line;
            }
            if (srtItem != null)
                result.add(srtItem);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
            return new ArrayList<>();
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
