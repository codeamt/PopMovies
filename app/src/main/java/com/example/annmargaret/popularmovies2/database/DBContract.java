package com.example.annmargaret.popularmovies2.database;


import android.provider.BaseColumns;



//DB Schema
public class DBContract {

    /* Empty Constructor */
    public DBContract() {
    }

    /* Inner Class for Single DB Entry */
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME = "display_name";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE = "released_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster_url";
        public static final String COLUMN_ID = "_id";
    }
}
