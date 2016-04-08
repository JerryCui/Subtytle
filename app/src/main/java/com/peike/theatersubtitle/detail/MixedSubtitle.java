package com.peike.theatersubtitle.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.peike.theatersubtitle.db.LocalSubtitle;
import com.peike.theatersubtitle.db.Subtitle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MixedSubtitle {
    public List<Subtitle> subtitleList;
    public List<LocalSubtitle> localSubtitleList;

    public MixedSubtitle() {
        subtitleList = new ArrayList<>();
        localSubtitleList = new ArrayList<>();
    }

    public void updateSubtitle(@NonNull List<Subtitle> subtitleList) {
        updateMixedSubtitle(subtitleList, this.localSubtitleList);
    }

    public void updateMixedSubtitle(@NonNull List<Subtitle> subtitleList,
                                    @NonNull List<LocalSubtitle> localSubtitleList) {
        this.subtitleList = new ArrayList<>();
        for (Subtitle sub : subtitleList) {
            boolean retainable = true;
            for (LocalSubtitle ls : localSubtitleList) {
                if (ls.getFileId().equals(sub.getFileId())) {
                    retainable = false;
                    break;
                }
            }
            if (retainable) this.subtitleList.add(sub);
        }
        this.localSubtitleList = localSubtitleList;
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
    public Subtitle getSubtitle(int index) {
        if (!localSubtitleList.isEmpty() && index < localSubtitleList.size()) {
            LocalSubtitle ls = localSubtitleList.get(index);
            return convertToSubtitle(ls);
        } else if (!subtitleList.isEmpty()) {
            if (!localSubtitleList.isEmpty()) {
                index -= localSubtitleList.size();
            }
            return subtitleList.get(index);
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

    public int markDownloaded(@NonNull Subtitle downloadedSubtitle) {
        int index = hasLocalSubtitle() ? localSubtitleList.size() : 0;
        Iterator<Subtitle> ite = subtitleList.iterator();
        while (ite.hasNext()) {
            Subtitle sub = ite.next();
            if (sub.getFileId().equals(downloadedSubtitle.getFileId())) {
                localSubtitleList = new ArrayList<>(localSubtitleList);
                localSubtitleList.add(convertToLocalSubtitle(sub));
                ite.remove();
                break;
            }
            index++;
        }
        return index;
    }

    public int markDeleted(Subtitle deletedSubtitle) {
        int index = 0;
        Iterator<LocalSubtitle> ite = localSubtitleList.iterator();
        while (ite.hasNext()) {
            LocalSubtitle sub = ite.next();
            if (sub.getFileId().equals(deletedSubtitle.getFileId())) {
                subtitleList = new ArrayList<>(subtitleList);
                subtitleList.add(convertToSubtitle(sub));
                ite.remove();
                break;
            }
            index++;
        }
        return index;
    }

    private LocalSubtitle convertToLocalSubtitle(Subtitle subtitle) {
        return new LocalSubtitle(0L, subtitle.getImdbId(), subtitle.getFileName(),
                subtitle.getLanguage(), subtitle.getDuration(), subtitle.getIso639(),
                subtitle.getAddDate(), subtitle.getFileSize(), subtitle.getDownloadCount(),
                subtitle.getFileId());
    }

    private Subtitle convertToSubtitle(LocalSubtitle ls) {
        return new Subtitle(0L, ls.getImdbId(), ls.getFileName(),
                ls.getLanguage(), ls.getDuration(), ls.getIso639(), ls.getAddDate(), ls.getFileSize(),
                ls.getDownloadCount(), ls.getFileId());
    }
}
