package com.peike.theatersubtitle.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class SettingsUtil {

    private static final String PREF_WELCOME_DONE = "pref_welcome_done";
    public static final String PREF_SUB_LANGUAGE = "pref_sub_language";

    private static final Set<String> DEFAULT_LANGUAGE = new HashSet<String>(){{
        add("English");
    }};

    public static Set<String> getLanguagePreference(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getStringSet(PREF_SUB_LANGUAGE, DEFAULT_LANGUAGE);
    }

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
