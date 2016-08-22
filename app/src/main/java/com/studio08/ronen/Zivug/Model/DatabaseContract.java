package com.studio08.ronen.Zivug.Model;

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
        public static final String COLUMN_NAME_ENTRY_UUID = "entryid";
        public static final String COLUMN_NAME_FULL_NAME = "name";
        public static final String COLUMN_NAME_FIRST_NAME = "first";
        public static final String COLUMN_NAME_LAST_NAME  = "last";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_IMAGE_RESOURCE = "image";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_TAGS = "tags";
        public static final String COLUMN_NAME_PREV_DATES = "dates";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_PHONE2 = "phone2";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_DATE_ADED = "date";
        public static final String COLUMN_NAME_NULLABLE = "nullable";

        // remember to change as well in DatabaseHelper in SQL_CREATE_ENTRIES
        // and the projection in ContactsRVFragment <-- made it null so it queries all columns
    }

    public static abstract class TagEntry implements BaseColumns {
        public static final String TABLE_NAME = "tag";
        public static final String COLUMN_NAME_ENTRY_UUID = "entryid";
        public static final String COLUMN_NAME_NAME = "name";
    }

}
