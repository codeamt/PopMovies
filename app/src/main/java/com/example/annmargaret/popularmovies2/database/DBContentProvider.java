package com.example.annmargaret.popularmovies2.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DBContentProvider extends ContentProvider {
    /* Vars */
    static String LOG_TAG="DBProvider";
    DBHelper dbHelper;
    SQLiteDatabase db;



    /* Uri Matcher */
    public static UriMatcher sUriMatcher() {
       final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
       matcher.addURI(DBConstants.AUTHORITY, DBConstants.MOVIES_PATH, DBConstants.MOVIE_LIST);
       matcher.addURI(DBConstants.AUTHORITY, DBConstants.MOVIE_DETAIL_PATH, DBConstants.MOVIE_DETAIL);
       return matcher;
    }



    /* Provider Overrides */
    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri ) {
        return null;
    }




    /* CRUD Methods */
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (sortOrder == null) sortOrder = DBContract.MovieEntry.COLUMN_ID;
        Cursor cursor;


        switch (sUriMatcher().match(uri)) {
            case DBConstants.MOVIE_DETAIL: {
                if (db != null) {
                    cursor = db.query(
                            DBContract.MovieEntry.TABLE_NAME,
                            projection,
                            DBConstants.COLUMN_ID_CLAUSE,
                            new String[]{uri.getLastPathSegment()},
                            null,
                            null,
                            sortOrder
                    );
                    break;
                }
            }

            case DBConstants.MOVIE_LIST: {
                if (db != null) {
                    cursor = db.query(
                            DBContract.MovieEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );
                    break;
                }
            }

            default:
                throw new UnsupportedOperationException("This query operation has not been implemented yet.");

        }

        if(getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }




    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri resultUri;
        long _id = db.insert(DBContract.MovieEntry.TABLE_NAME
                , null, values);
        switch(sUriMatcher().match(uri)){
            case DBConstants.MOVIE_DETAIL:
                resultUri = ContentUris.withAppendedId(DBConstants.CONTENT_URI, _id);
                if(getContext() != null) {
                    getContext().getContentResolver().notifyChange(resultUri, null);
                }
                return resultUri;
            default:
                return null;
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch(sUriMatcher().match(uri)) {
            case DBConstants.MOVIE_LIST: {
                count = db.delete(DBContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case DBConstants.MOVIE_DETAIL: {
                count = db.delete(DBContract.MovieEntry.TABLE_NAME, DBConstants.COLUMN_ID_CLAUSE, selectionArgs);
                break;
            }

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Updating feature not implemented yet.");
    }
}
