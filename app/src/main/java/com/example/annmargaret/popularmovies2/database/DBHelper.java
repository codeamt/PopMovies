package com.example.annmargaret.popularmovies2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.annmargaret.popularmovies2.database.DBContract.MovieEntry;

public class DBHelper extends SQLiteOpenHelper {


    /* Constructor */
    public DBHelper(Context c) {
        super(c, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    /* DB Overrides */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("TABLE", "creating table " + DBConstants.CREATE_TABLE);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("delete from " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL(DBConstants.DROP_TB);
        onCreate(sqLiteDatabase);
    }

}
