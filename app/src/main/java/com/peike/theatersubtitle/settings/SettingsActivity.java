package com.peike.theatersubtitle.settings;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.cache.InternalFileCache;
import com.peike.theatersubtitle.db.DaoHelper;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.DialogUtil;
import com.peike.theatersubtitle.util.SnackUtil;

public class SettingsActivity extends BaseActivity {

    public interface FragView {

        void setSummary(String summary);
    }
    private View rootView;

    private FragView fragView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getToolBar(HAS_BACK_BUTTON);

        rootView = findViewById(android.R.id.content);
        PreferenceManager.setDefaultValues(this, R.xml.settings_prefs, false);
        setupSummary();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof SettingsFragment) {
            fragView = (FragView) fragment;
        }
    }

    @Override
    protected boolean canShowBackButton() {
        return true;
    }

    public void onStoragePreferenceClicked() {
        if (hasNoCache()) {
            SnackUtil.show(rootView, getString(R.string.pref_storage_empty));
        } else {
            String confirmMessage = getString(R.string.pref_storage_clear_confirm);
            DialogUtil.confirm(this, confirmMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        onConfirmClearStorageClicked();
                    }
                }
            });
        }
    }

    private void onConfirmClearStorageClicked() {
        deleteAllSubtitleFiles();
        removeLocalSubDbCache();

        setupSummary();
        SnackUtil.show(rootView, getString(R.string.pref_storage_cleared));
    }

    private void removeLocalSubDbCache() {
        DaoHelper daoHelper = new DaoHelper();
        daoHelper.deleteAllLocalSubtitle();
    }

    private void deleteAllSubtitleFiles() {
        InternalFileCache fileCache = AppApplication.getInternalFileCache();
        fileCache.deleteAllSubtitleFiles();
    }

    private void setupSummary() {
        InternalFileCache fileCache = AppApplication.getInternalFileCache();
        int fileNumber = fileCache.getSubtitleFileNumber();
        String pluralized = getPluralized(fileNumber);
        long fileTotalSizeInByte = fileCache.getTotalSubtitleFileSizeInByte();
        String fileTotalSize = getProperSizeUnit(fileTotalSizeInByte);
        String formattedSummary = getString(
                R.string.pref_storage_summary,
                fileNumber,
                pluralized,
                fileTotalSize);

        fragView.setSummary(formattedSummary);
    }

    private String getPluralized(int fileNumber) {
        return fileNumber > 1 ? getString(R.string.subtitles) : getString(R.string.subtitle);
    }

    private String getProperSizeUnit(long fileTotalSizeInByte) {
        if (fileTotalSizeInByte < 1024L) {
            return fileTotalSizeInByte + Constants.UNIT_BYTES;
        } else if (fileTotalSizeInByte / Constants.ONE_KB < 1024L) {
            return fileTotalSizeInByte / Constants.ONE_KB + Constants.UNIT_KB;
        } else if (fileTotalSizeInByte / Constants.ONE_MB < 1024L) {
            return fileTotalSizeInByte / Constants.ONE_MB + Constants.UNIT_MB;
        } else {
            return fileTotalSizeInByte / Constants.ONE_GB + Constants.UNIT_GB;
        }
    }

    private boolean hasNoCache() {
        InternalFileCache internalFileCache = AppApplication.getInternalFileCache();
        return internalFileCache.getSubtitleFileNumber() == 0;
    }
}
