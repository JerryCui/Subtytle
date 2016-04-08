package com.peike.theatersubtitle.cache;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class InternalFileCache {

    private static final String PREFIX = "subtytle_";
    private static final String SUFFIX = ".srt";
    private final Context context;

    public InternalFileCache(Context context) {
        this.context = context;
    }

    public void writeToInternal(String fileId, String fileContent) {
        Writer out = null;
        try {
            String fileName = getFileName(fileId);
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            out = new BufferedWriter(new OutputStreamWriter(
                    fos, Charset.defaultCharset()));
            out.write(handleBOM(fileContent));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String handleBOM(String content) {
        if (content.charAt(0) == '\ufeff') {
            return content.substring(1);
        }
        return content;
    }

    public boolean isFileExist(String fileId) {
        File file = context.getFileStreamPath(getFileName(fileId));
        return file.exists();
    }

    public FileInputStream readStreamFromInternal(String fileId) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(getFileName(fileId));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }

    public long getTotalSubtitleFileSizeInByte() {
        String[] fileNames = context.fileList();
        long fileSizeInBytes = 0L;
        for (String fileName : fileNames) {
            if (!isSubtitleFile(fileName)) continue;
            File file = context.getFileStreamPath(fileName);
            fileSizeInBytes += file.length();
        }
        return fileSizeInBytes;
    }

    public int getSubtitleFileCount() {
        String[] allFiles =  context.fileList();
        int count = 0;
        for (String fileName : allFiles) {
            if (isSubtitleFile(fileName)) {
                count++;
            }
        }
        return count;
    }

    public void deleteAllSubtitleFiles() {
        String[] fileNames = context.fileList();
        for (String fileName : fileNames) {
            if (isSubtitleFile(fileName)) {
                context.deleteFile(fileName);
            }
        }
    }

    public void deleteSubtitle(String fileId) {
        String fileName = getFileName(fileId);
        context.deleteFile(fileName);
    }

    private boolean isSubtitleFile(String fileName) {
        return fileName.startsWith(PREFIX) && fileName.endsWith(SUFFIX);
    }

    private String getFileName(String fileId) {
        return PREFIX + fileId + SUFFIX;
    }
}
