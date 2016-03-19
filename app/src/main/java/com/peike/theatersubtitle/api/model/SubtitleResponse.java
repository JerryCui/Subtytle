package com.peike.theatersubtitle.api.model;

import com.google.gson.annotations.SerializedName;

public class SubtitleResponse implements Response {
    @SerializedName(value = "imdbId", alternate = "imdb_id")
    public String imdbId;
    @SerializedName(value = "fileName", alternate = "file_name")
    public String fileName;
    @SerializedName("language")
    public String language;
    @SerializedName("duration")
    public String duration;
    @SerializedName(value = "iso639", alternate = "ISO639")
    public String iso639;
    @SerializedName(value = "addDate", alternate = "add_date")
    public String addDate;
    @SerializedName(value = "fileSize", alternate = "file_size")
    public Integer fileSize;
    @SerializedName(value = "downloadCount", alternate = "download_count")
    public Integer downloadCount;
    @SerializedName(value = "fileId", alternate = "file_id")
    public Integer fileId;
}
