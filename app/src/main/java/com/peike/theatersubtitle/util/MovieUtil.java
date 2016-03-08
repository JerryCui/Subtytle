package com.peike.theatersubtitle.util;

import java.text.NumberFormat;

public class MovieUtil {

    private static final String N_A = "N/A";

    public static String formatImdbRating(String imdbScore) {
        if (imdbScore.equalsIgnoreCase(N_A)) {
            return imdbScore;
        } else {
            return imdbScore + "/10";
        }
    }

    public static String formatTomatoRating(String tomatoMeterScore) {
        if (tomatoMeterScore.equalsIgnoreCase(N_A)) {
            return tomatoMeterScore;
        } else {
            return tomatoMeterScore + "/100";
        }
    }

    public static String byteToKB(int b) {
        double byteValue = (double) b;
        return String.format("%.2f", byteValue / 1024D) + " KB";
    }

    public static String formatNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }
}
