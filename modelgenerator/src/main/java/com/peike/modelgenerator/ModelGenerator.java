package com.peike.modelgenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ModelGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.peike.theatersubtitle.model");

        addMovie(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addMovie(Schema schema) {
        Entity movie = schema.addEntity("Movie");
        movie.addIdProperty();
        movie.addStringProperty("title").notNull();
        movie.addStringProperty("posterUrl");
        movie.addStringProperty("backdropUrl");
        movie.addStringProperty("imdbId");
        movie.addStringProperty("imdbRating");
        movie.addStringProperty("tomatoRating");
    }
}
