package com.peike.theatersubtitle.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageUtil {
    private static final String LOG_TAG = "LanguageUtil";
    private static final String iso639FileUri = "iso639.csv";
    private static Map<String, String> nameIsoMap;
    private static Map<String, String> isoNameMap;
    private static LanguageUtil instance;

    public static LanguageUtil newInstance(Context context) {
        if (instance == null) {
            instance = new LanguageUtil(context);
        }
        return instance;
    }

    private LanguageUtil(Context context) {
        BufferedReader reader = null;
        nameIsoMap = new HashMap<>();
        isoNameMap = new HashMap<>();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(iso639FileUri)));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                String[] tmp = mLine.split("\t");
                nameIsoMap.put(tmp[2].toLowerCase(), tmp[0]);
                isoNameMap.put(tmp[0], tmp[2]);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Fail to process csv file");
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    Log.d(LOG_TAG, "Successfully read " + nameIsoMap.size() + " entries");
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Fail to close reader");
                }
            }
        }
    }

    public Collection<String> nameToIso639_2(Collection<String> names) {
        List<String> isoCodes = new ArrayList<>();
        for (String name : names) {
            String isoCode = nameToIso639_2(name.toLowerCase());
            if (isoCode != null) {
                isoCodes.add(isoCode);
            }
        }
        return isoCodes;
    }

    public String[] iso639_2ToName(Collection<String> langCodes) {
        List<String> langNames = new ArrayList<>();
        for (String codes : langCodes) {
            String name = isoNameMap.get(codes.toLowerCase());
            if (name != null) {
                langNames.add(name);
            }
        }
        return langNames.toArray(new String[langNames.size()]);
    }

    public String nameToIso639_2(String langName) {
        Log.d(LOG_TAG, "Convert " + langName);
        return nameIsoMap.get(langName);
    }
}
