package com.peike.theatersubtitle.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {
    /**
     * Convert data with format of '01:20:58,362' to milliseconds
     * @param timecode timecode in .srt subtitle file
     * @return milliseconds
     */
    public static int timecodeToMillisecond(String timecode) {
        int commaIdx = timecode.lastIndexOf(',');
        String seconds = timecode.substring(0, commaIdx);
        String milliSeconds = timecode.substring(commaIdx + 1);
        String[] times = seconds.split(":");

        int result = 3600000 * Integer.valueOf(times[0]) +
                        60000 * Integer.valueOf(times[1]) +
                        1000 * Integer.valueOf(times[2]);

        return result + Integer.valueOf(milliSeconds);
    }

    /**
     * Support time smaller than 20:31:23,647
     * @param time time in the format <CODE>02:03:02,002</CODE>
     * @return millisecond
     * @throws ParseException
     */
    public static int timeToMillisecond(String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss,SSS", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = formatter.parse(time);
        return (int) date.getTime();
    }

    /**
     * Convert millisecond to time format
     * @param millis millisecond
     * @return in the format of 02:03:03,002
     */
    public static String millisToHourMinuteSecondMillis(int millis) {
        int millisecond = millis % 1000;
        int second = (millis / 1000) % 60;
        int minute = (millis / (1000 * 60)) % 60;
        int hour = (millis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d,%d", hour, minute, second, millisecond);
    }

    /**
     *
     * @param millisecond time in milliseconds
     * @return in the format of hour:minute, e.g. 02:47
     */
    public static String millisToHourMinuteSecond(int millisecond) {
        int second = (millisecond / 1000) % 60;
        int minute = (millisecond / (1000 * 60)) % 60;
        int hour = (millisecond / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public static int getCurrentWeekOfYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.WEEK_OF_YEAR);
    }
}
