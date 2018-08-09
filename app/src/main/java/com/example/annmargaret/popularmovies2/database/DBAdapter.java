package com.example.annmargaret.popularmovies2.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


import com.example.annmargaret.popularmovies2.models.Movie;

import java.util.ArrayList;
import java.util.List;


public class DBAdapter {

    /*Vars*/
    DBHelper helper;




    /* Constructor */
    public DBAdapter(Context context) {
        helper = new DBHelper(context);
    }







    /***************
     *
     * Helpers
     * Note: The DB Design Pattern for this app persists only movies marked favorite
     *
     ****************/

    public boolean isMovieFavorited(ContentResolver contentResolver, int id) {
        boolean ret = false;
        Cursor cursor = contentResolver.query(Uri.parse(DBConstants.AUTHORITY_URI + "/" + id), null, null, null, null, null);
        if(cursor != null && cursor.moveToNext()) {
            ret = true;
            cursor.close();
        }
        return ret;
    }


    public List<Movie> getFavoriteMovies(ContentResolver contentResolver) {
        Uri uri = Uri.parse(DBConstants.AUTHORITY_URI + "/movies");
        Cursor cursor = contentResolver.query(uri, null, null, null, null, null);
        List<Movie> movies = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.id = cursor.getInt(cursor.getColumnIndex(DBContract.MovieEntry.COLUMN_ID));
                movie.displayName = cursor.getString(cursor.getColumnIndex(DBContract.MovieEntry.COLUMN_NAME));
                movie.overview = cursor.getString(cursor.getColumnIndex(DBContract.MovieEntry.COLUMN_OVERVIEW));
                movie.rating = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DBContract.MovieEntry.COLUMN_RATING)));
                movie.posterUrl = cursor.getString(cursor.getColumnIndex(DBContract.MovieEntry.COLUMN_POSTER));
                movie.releasedDate = cursor.getString(cursor.getColumnIndex(DBContract.MovieEntry.COLUMN_RELEASE));
                movies.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return movies;
    }


    public void addMovie(ContentResolver contentResolver, Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.MovieEntry.COLUMN_ID, movie.id);
        contentValues.put(DBContract.MovieEntry.COLUMN_NAME, movie.displayName);
        contentValues.put(DBContract.MovieEntry.COLUMN_OVERVIEW, movie.overview);
        contentValues.put(DBContract.MovieEntry.COLUMN_POSTER, movie.posterUrl);
        contentValues.put(DBContract.MovieEntry.COLUMN_RATING, movie.rating + "");
        contentValues.put(DBContract.MovieEntry.COLUMN_RELEASE, movie.releasedDate);
        contentResolver.insert(Uri.parse(DBConstants.AUTHORITY_URI + "/movies"), contentValues);
    }



    public void removeMovie(ContentResolver contentResolver, int id) {
        Uri uri =  Uri.parse(DBConstants.AUTHORITY_URI + "/" + id);
        contentResolver.delete(uri, null, new String[] {id + ""});
    }




}
