package com.peike.theatersubtitle.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.peike.theatersubtitle.settings.SettingsActivity;

import java.util.HashSet;

/**
 * Created by Peike on 2/19/2016.
 */
public class SettingsUtil {

    private static final String PREF_WELCOME_DONE = "pref_welcome_done";
    public static final String PREF_SUB_LANGUAGE = "pref_sub_language";

    public static void setDefaultLanguage(final Context context, final String deviceLanguage) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putStringSet(PREF_SUB_LANGUAGE, new HashSet<String>() {{
            add(deviceLanguage);
        }}).apply();
    }

    public static boolean isFirstRunProcessComplete(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_WELCOME_DONE, false);
    }

    public static void markFirstRunProcessesDone(final Context context, boolean newValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_WELCOME_DONE, newValue).apply();
    }

    public static void registerOnSharedPreferenceChangeListener(final Context context,
                                                                SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }
}
