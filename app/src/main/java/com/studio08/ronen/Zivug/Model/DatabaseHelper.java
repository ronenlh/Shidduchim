package com.studio08.ronen.Zivug.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ronen on 8/8/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // methods that create and maintain the database and tables
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = " , ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE VIRTUAL TABLE " + DatabaseContract.Entry.TABLE_NAME + " USING fts3 ( " +
                    DatabaseContract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_FULL_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_GENDER + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_AGE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_IMAGE_RESOURCE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_NOTES + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_TAGS + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_PREV_DATES + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_PHONE2 + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_DATE_ADED + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Entry.COLUMN_NAME_NULLABLE + TEXT_TYPE +
                    "FOREIGN KEY( " + DatabaseContract.Entry.COLUMN_NAME_TAGS + " ) REFERENCES "+DatabaseContract.TagEntry.TABLE_NAME+"( "+DatabaseContract.TagEntry._ID+" ) " +
                    " )";

    private static final String SQL_TAGS_TABLE = "CREATE TABLE " + DatabaseContract.TagEntry.TABLE_NAME + " (" +
            DatabaseContract.TagEntry._ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, \n" +
            DatabaseContract.TagEntry.COLUMN_NAME_ENTRY_UUID + TEXT_TYPE + COMMA_SEP +
            DatabaseContract.TagEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            DatabaseContract.TagEntry.COLUMN_NAME_NULLABLE + TEXT_TYPE +
            ")";

    private static final String SQL_LOCATION_TABLE = "CREATE TABLE " + DatabaseContract.LocationEntry.TABLE_NAME + " (" +
            DatabaseContract.TagEntry._ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, \n" +
            DatabaseContract.TagEntry.COLUMN_NAME_ENTRY_UUID + TEXT_TYPE + COMMA_SEP +
            DatabaseContract.TagEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            DatabaseContract.TagEntry.COLUMN_NAME_NULLABLE + TEXT_TYPE +
            ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.Entry.TABLE_NAME;

    private static final String SQL_TAG_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.TagEntry.TABLE_NAME;

    private static final String SQL_LOCATION_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.LocationEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "Zivug.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_TAGS_TABLE);
        db.execSQL(SQL_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_TAG_DELETE_ENTRIES);
        db.execSQL(SQL_LOCATION_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
