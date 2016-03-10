package com.peike.theatersubtitle.cache;

import android.content.Context;

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

    private final Context context;

    public InternalFileCache(Context context) {
        this.context = context;
    }

    public void writeToInternal(String fileName, String fileContent) {
        Writer out = null;
        try {
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

    public String readFromInternal(String fileName) {
        String result = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isFileExist(String fileName) {
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    public FileInputStream readStreamFromInternal(String fileName) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }

}
