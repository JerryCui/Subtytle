package com.peike.modelgenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ModelGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.peike.theatersubtitle.db");

        addMovie(schema);
        addSubtitle(schema);
        addLocalSubtitle(schema);
        addMovieSearch(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addMovie(Schema schema) {
        Entity movie = schema.addEntity("Movie");
        movie.addIdProperty();
        movie.addStringProperty("title");
        movie.addStringProperty("posterUrl");
        movie.addStringProperty("backdropUrl");
        movie.addStringProperty("imdbId").unique().notNull();
        movie.addStringProperty("imdbRating");
        movie.addStringProperty("tomatoRating");
        movie.addStringProperty("moviePlot");
        movie.addStringProperty("boxOffice");
        movie.addIntProperty("revision");
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
        subtitle.addIntProperty("fileId").unique();
    }

    private static void addLocalSubtitle(Schema schema) {
        Entity localSubtitle = schema.addEntity("LocalSubtitle");
        localSubtitle.addIdProperty();
        localSubtitle.addStringProperty("imdbId");
        localSubtitle.addStringProperty("fileName");
        localSubtitle.addStringProperty("language");
        localSubtitle.addStringProperty("duration");
        localSubtitle.addStringProperty("iso639");
        localSubtitle.addStringProperty("addDate");
        localSubtitle.addIntProperty("fileSize");
        localSubtitle.addIntProperty("downloadCount");
        localSubtitle.addIntProperty("fileId").unique();
    }

    private static void addMovieSearch(Schema schema) {
        Entity movieSearchResult = schema.addEntity("MovieSearchResult");
        movieSearchResult.addIdProperty();
        movieSearchResult.addStringProperty("title");
        movieSearchResult.addStringProperty("posterUrl");
        movieSearchResult.addStringProperty("backdropUrl");
        movieSearchResult.addStringProperty("imdbId").unique();
        movieSearchResult.addStringProperty("imdbRating");
        movieSearchResult.addStringProperty("tomatoRating");
        movieSearchResult.addStringProperty("query");
        movieSearchResult.addLongProperty("expiredAt");
    }
}
