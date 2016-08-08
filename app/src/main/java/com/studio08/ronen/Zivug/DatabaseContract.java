package com.studio08.ronen.Zivug;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Ronen on 8/8/16.
 * https://developer.android.com/training/basics/data-storage/databases.html
 */

public class DatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_TAGS = "tags";
        public static final String COLUMN_NAME_PREV_DATES = "dates";
        public static final String COLUMN_NAME_NULLABLE = "nullable";
    }

}
