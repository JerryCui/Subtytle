package com.peike.modelgenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ModelGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.peike.theatersubtitle.db");

        addMovie(schema);
        addSubtitle(schema);
        addMovieSearch(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addMovie(Schema schema) {
        Entity movie = schema.addEntity("Movie");
        movie.addIdProperty();
        movie.addStringProperty("title");
        movie.addStringProperty("posterUrl");
        movie.addStringProperty("backdropUrl");
        movie.addStringProperty("imdbId");
        movie.addStringProperty("imdbRating");
        movie.addStringProperty("tomatoRating");
        movie.addStringProperty("moviePlot");
        movie.addStringProperty("boxOffice");
    }

    private static void addSubtitle(Schema scheme) {
        Entity subtitle = scheme.addEntity("Subtitle");
        subtitle.addIdProperty();
        subtitle.addStringProperty("imdbId");
        subtitle.addStringProperty("fileName");
        subtitle.addStringProperty("language");
        subtitle.addStringProperty("duration");
        subtitle.addStringProperty("iso639");
        subtitle.addStringProperty("addDate");
        subtitle.addIntProperty("fileSize");
        subtitle.addIntProperty("downloadCount");
        subtitle.addIntProperty("fileId");
    }

    private static void addMovieSearch(Schema schema) {
        Entity movieSearchResult = schema.addEntity("MovieSearchResult");
        movieSearchResult.addIdProperty();
        movieSearchResult.addStringProperty("title");
        movieSearchResult.addStringProperty("posterUrl");
        movieSearchResult.addStringProperty("backdropUrl");
        movieSearchResult.addStringProperty("imdbId");
        movieSearchResult.addStringProperty("imdbRating");
        movieSearchResult.addStringProperty("tomatoRating");
        movieSearchResult.addStringProperty("query");
        movieSearchResult.addLongProperty("expiredAt");
    }
}
