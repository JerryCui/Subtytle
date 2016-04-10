package com.peike.theatersubtitle.util;

import android.util.Log;

import java.util.Locale;

public class DeviceUtil {
    public static String getDeviceIso639_2() {
        String iso639_2 = Locale.getDefault().getISO3Language();
        Log.d("DeviceUtil","Device Language in ISO639_2: " + iso639_2);
        return handleSpecialCases(iso639_2);
    }

    private static String handleSpecialCases(String iso639_2) {
        iso639_2 = iso639_2.toLowerCase();
        switch (iso639_2) {
            case "zho":
                return "chi";
            case "sqi":
                return "alb";
            case "ron":
                return "rum";
            case "fas":
                return "per";
            case "nld":
                return "dut";
            case "msa":
                return "may";
            case "deu":
                return "ger";
            case "fra":
                return "fre";
            case "ces":
                return "cze";
            default:
                return iso639_2;
        }
    }
}
