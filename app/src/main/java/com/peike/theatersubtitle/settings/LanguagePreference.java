package com.peike.theatersubtitle.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.MultiSelectListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.peike.theatersubtitle.util.LanguageUtil;

import java.util.Set;

public class LanguagePreference extends MultiSelectListPreference {
    public LanguagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
    }

    @Override
    public CharSequence getSummary() {
        Set<String> entries = getValues();
        if (entries == null) {
            return null;
        } else {
            return TextUtils.join(", ", LanguageUtil.newInstance(getContext()).iso639_2ToName(entries));
        }
    }

    @Override
    public void setValues(Set<String> values) {
        super.setValues(values);
        notifyChanged();
    }
}
