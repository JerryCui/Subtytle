package com.peike.theatersubtitle.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.peike.theatersubtitle.db.Movie;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MOVIE".
*/
public class MovieDao extends AbstractDao<Movie, Long> {

    public static final String TABLENAME = "MOVIE";

    /**
     * Properties of entity Movie.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property PosterUrl = new Property(2, String.class, "posterUrl", false, "POSTER_URL");
        public final static Property BackdropUrl = new Property(3, String.class, "backdropUrl", false, "BACKDROP_URL");
        public final static Property ImdbId = new Property(4, String.class, "imdbId", false, "IMDB_ID");
        public final static Property ImdbRating = new Property(5, String.class, "imdbRating", false, "IMDB_RATING");
        public final static Property TomatoRating = new Property(6, String.class, "tomatoRating", false, "TOMATO_RATING");
        public final static Property MoviePlot = new Property(7, String.class, "moviePlot", false, "MOVIE_PLOT");
        public final static Property BoxOffice = new Property(8, String.class, "boxOffice", false, "BOX_OFFICE");
        public final static Property Week = new Property(9, Integer.class, "week", false, "WEEK");
    };


    public MovieDao(DaoConfig config) {
        super(config);
    }
    
    public MovieDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MOVIE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TITLE\" TEXT," + // 1: title
                "\"POSTER_URL\" TEXT," + // 2: posterUrl
                "\"BACKDROP_URL\" TEXT," + // 3: backdropUrl
                "\"IMDB_ID\" TEXT NOT NULL UNIQUE ," + // 4: imdbId
                "\"IMDB_RATING\" TEXT," + // 5: imdbRating
                "\"TOMATO_RATING\" TEXT," + // 6: tomatoRating
                "\"MOVIE_PLOT\" TEXT," + // 7: moviePlot
                "\"BOX_OFFICE\" TEXT," + // 8: boxOffice
                "\"WEEK\" INTEGER);"); // 9: week
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MOVIE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Movie entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String posterUrl = entity.getPosterUrl();
        if (posterUrl != null) {
            stmt.bindString(3, posterUrl);
        }
 
        String backdropUrl = entity.getBackdropUrl();
        if (backdropUrl != null) {
            stmt.bindString(4, backdropUrl);
        }
        stmt.bindString(5, entity.getImdbId());
 
        String imdbRating = entity.getImdbRating();
        if (imdbRating != null) {
            stmt.bindString(6, imdbRating);
        }
 
        String tomatoRating = entity.getTomatoRating();
        if (tomatoRating != null) {
            stmt.bindString(7, tomatoRating);
        }
 
        String moviePlot = entity.getMoviePlot();
        if (moviePlot != null) {
            stmt.bindString(8, moviePlot);
        }
 
        String boxOffice = entity.getBoxOffice();
        if (boxOffice != null) {
            stmt.bindString(9, boxOffice);
        }
 
        Integer week = entity.getWeek();
        if (week != null) {
            stmt.bindLong(10, week);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Movie readEntity(Cursor cursor, int offset) {
        Movie entity = new Movie( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // posterUrl
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // backdropUrl
            cursor.getString(offset + 4), // imdbId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // imdbRating
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // tomatoRating
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // moviePlot
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // boxOffice
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9) // week
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Movie entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPosterUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBackdropUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImdbId(cursor.getString(offset + 4));
        entity.setImdbRating(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTomatoRating(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMoviePlot(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBoxOffice(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setWeek(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Movie entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Movie entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
