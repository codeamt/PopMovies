package com.example.annmargaret.popularmovies2.database;

import android.net.Uri;

public class DBConstants {

    //db helper constants
    static final String DATABASE_NAME = "movies.db";
    static final int DATABASE_VERSION = 4;
    public static final String AUTHORITY = "com.example.annmargaret.popularmovies2";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String AUTHORITY_URI = "content://" + AUTHORITY;
    public static final String MOVIES_PATH="movies";
    public static final String MOVIE_DETAIL_PATH="#";
    public static final int MOVIE_DETAIL = 2;
    public static final int MOVIE_LIST = 1;
    public static final String COLUMN_ID_CLAUSE=DBContract.MovieEntry.COLUMN_ID + " =?";


    //sql commands
    static final String CREATE_TABLE = "create table " + DBContract.MovieEntry.TABLE_NAME + " (" +
    DBContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
    DBContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
    DBContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
    DBContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
    DBContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
    DBContract.MovieEntry.COLUMN_RELEASE + " TEXT NOT NULL " +
            ")";

    static final String DROP_TB="DROP TABLE IF EXISTS " + DBContract.MovieEntry.TABLE_NAME;
}
