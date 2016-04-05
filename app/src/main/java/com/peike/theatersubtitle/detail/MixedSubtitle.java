package com.peike.theatersubtitle.detail;

import android.support.annotation.Nullable;

import com.peike.theatersubtitle.db.LocalSubtitle;
import com.peike.theatersubtitle.db.Subtitle;

import java.util.ArrayList;
import java.util.List;

public class MixedSubtitle {
    public List<Subtitle> subtitleList;
    public List<LocalSubtitle> localSubtitleList;

    public MixedSubtitle() {
        subtitleList = new ArrayList<>();
        localSubtitleList = new ArrayList<>();
    }

    public int getLocalSubtitleCount() {
        return localSubtitleList.size();
    }

    public int getDownloadableSubtitleCount() {
        return subtitleList.size();
    }

    public int getTotalCount() {
        return subtitleList.size() + localSubtitleList.size();
    }

    @Nullable
    public Subtitle getSubtitle(int position) {
        if (!localSubtitleList.isEmpty() && position < localSubtitleList.size()) {
            LocalSubtitle ls = localSubtitleList.get(position);
            return new Subtitle(0L, ls.getImdbId(), ls.getFileName(),
                    ls.getLanguage(), ls.getDuration(), ls.getIso639(), ls.getAddDate(), ls.getFileSize(),
                    ls.getDownloadCount(), ls.getFileId());
        } else if (!subtitleList.isEmpty()) {
            if (!localSubtitleList.isEmpty()) {
                position -= localSubtitleList.size();
            }
            return subtitleList.get(position);
        } else {
            return null;
        }
    }

    public boolean hasLocalSubtitle() {
        return !localSubtitleList.isEmpty();
    }

    public boolean hasDownloadableSubtitle() {
        return !subtitleList.isEmpty();
    }
}
