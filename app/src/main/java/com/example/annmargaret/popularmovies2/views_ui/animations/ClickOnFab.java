package com.example.annmargaret.popularmovies2.views_ui.animations;

import android.content.ContentResolver;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.example.annmargaret.popularmovies2.database.DBAdapter;
import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.models.Movie;
import com.example.annmargaret.popularmovies2.views_ui.MainActivity;

public class ClickOnFab implements View.OnClickListener {
    /* Views + Widgets */
    FloatingActionButton fab;
    MainActivity mainActivity = MainActivity.instance;
    Context mContext;


    /* Models */
    Movie movie;



    /* Constructor */
    public ClickOnFab(FloatingActionButton fabB, Movie targetMovie, Context c) {
        this.fab = fabB;
        this.movie = targetMovie;
        this.mContext = c;
    }




    /* Overrides */
    @Override
    public void onClick(View view) {

        /* Vars */
        ContentResolver contentResolver = mContext.getContentResolver();
        DBAdapter moviesDB = new DBAdapter(mContext);
        String message;


        /* Check if Movie has been persisted to DB and Illuminate Star */
        if (moviesDB.isMovieFavorited(contentResolver, movie.id)) {
            message = mContext.getString(R.string.unfav_text);
            moviesDB.removeMovie(contentResolver, movie.id);
            fab.setImageDrawable(ContextCompat.getDrawable(mContext, android.R.drawable.btn_star_big_off));
            //Log.v(LOG_TAG, movie.displayName + message);
        } else {
            /* Else Dull Fab Star */
            moviesDB.addMovie(contentResolver, movie);
            message = mContext.getString(R.string.fav_text);
            fab.setImageDrawable(ContextCompat.getDrawable(mContext, android.R.drawable.btn_star_big_on));
            //Log.v(LOG_TAG, movie.displayName + message);
        }


        //Log.v(LOG_TAG, "Favorites grid updated.");
        //Toast favorited status
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
