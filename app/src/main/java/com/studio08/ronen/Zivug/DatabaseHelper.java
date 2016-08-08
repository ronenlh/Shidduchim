package com.studio08.ronen.Zivug;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ronen on 8/8/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // methods that create and maintain the database and tables
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.Entry.TABLE_NAME + " (" +
                    DatabaseContract.Entry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.Entry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_AGE + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_NOTES + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_TAGS + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_PREV_DATES + TEXT_TYPE + COMMA_SEP +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.Entry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Zivug.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
