package com.studio08.ronen.Zivug.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 * Class responsible for everything related to persisting data in the app
 */

public class ContactLab {
    private static ContactLab mContactLab;
    public static String TAG_NAME = "tags";
    public static String LOCATION_NAME = "locations";
    private final String TAG = "ContactLab";

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ContactLab get(Context context) {
        if (mContactLab == null) {
            mContactLab = new ContactLab(context);
        }
        return  mContactLab;
    }

    public ContactLab(Context context) {
        this.mContext = context.getApplicationContext(); // because it is a singleton, we use the application context so we don't maintain a link to an activity than should be garbage collected.
        this.mDatabase = new DatabaseHelper(mContext).getWritableDatabase(); // when should this db be closed?
        Log.d(TAG, "ContactLab created");
    }

    public void addContact(Contact contact) {
        ContentValues values = getContentValues(contact);
        mDatabase.insert(DatabaseContract.Entry.TABLE_NAME,
                DatabaseContract.Entry.COLUMN_NAME_NULLABLE,
                values);
    }

    public void addTag(Tag tag) {
        ContentValues values = getContentValues(tag);
        mDatabase.insert(DatabaseContract.TagEntry.TABLE_NAME,
                DatabaseContract.TagEntry.COLUMN_NAME_NULLABLE,
                values);
    }

    public void addLocation(Location location) {
        ContentValues values = getContentValues(location);
        mDatabase.insert(DatabaseContract.LocationEntry.TABLE_NAME,
                DatabaseContract.LocationEntry.COLUMN_NAME_NULLABLE,
                values);
    }

    public void addPreviousDateToDb(Contact contact, Contact date) {
        // this isn't about adding a new row to a table but appending a string to a value
        contact.addPreviousDate(date);
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Entry.COLUMN_NAME_PREV_DATES, contact.getPreviousDatesAsString());

        mDatabase.update(DatabaseContract.TagEntry.TABLE_NAME,
                values,
                null,
                null);
    }

    public void updateContact(Contact contact) {
        String uuidString = contact.getId().toString();
        ContentValues values = getContentValues(contact);
        mDatabase.update(DatabaseContract.Entry.TABLE_NAME,
                values, DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID + " = ?", // this is a WHERE clause, we use ? to avoid SQL injection from the literal string
                new String[] {uuidString});
    }

    public void updateTag(Tag tag) {
        String uuidString = tag.getId().toString();
        ContentValues values = getContentValues(tag);
        mDatabase.update(DatabaseContract.TagEntry.TABLE_NAME,
                values, DatabaseContract.TagEntry.COLUMN_NAME_ENTRY_UUID + " = ?", // this is a WHERE clause, we use ? to avoid SQL injection from the literal string
                new String[] {uuidString});
    }

    public void updateLocation(Location location) {
        String uuidString = location.getId().toString();
        ContentValues values = getContentValues(location);
        mDatabase.update(DatabaseContract.LocationEntry.TABLE_NAME,
                values, DatabaseContract.LocationEntry.COLUMN_NAME_ENTRY_UUID + " = ?", // this is a WHERE clause, we use ? to avoid SQL injection from the literal string
                new String[] {uuidString});
    }


    public ContactCursorWraper getWordMatches(String query) {

        if (query.isEmpty()) return queryContactsTable(null, null);

        String selection = DatabaseContract.Entry.TABLE_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

//        if (query.isEmpty()) return queryContacts(null, null);

        return queryContactsTable(selection, selectionArgs);
    }

    public Cursor getTagMatches(Tag[] tags, int genderParam) {

        // SELECT * FROM entry WHERE tags LIKE '%817d944e-0ad0-491f-b9a5-121448926097%' AND  tags LIKE '%507e4f71-9d2b-465b-b932-94754a4d1992%'

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT * FROM " + DatabaseContract.Entry.TABLE_NAME
                + " WHERE " + DatabaseContract.Entry.COLUMN_NAME_GENDER + " MATCH " + genderParam);

        if(tags.length > 0)
            for (int i = 0; i < tags.length; i++) {
                // here I construct the query arguments based on the arguments
                sqlQuery.append(" AND " + DatabaseContract.Entry.COLUMN_NAME_TAGS + " LIKE '%" + tags[i].getId().toString() + "%'");
            }

        sqlQuery.append(";");

        Log.d(TAG, "getTagMatches: query: " + sqlQuery.toString());

        DatabaseHelper mDatabaseOpenHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery.toString(), null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) { // cursor.moveToFirst() return false if cursor is empty
            cursor.close();
            return null;
        }
        db.close();
        return new ContactCursorWraper(cursor, null);
    }

    public Cursor getLocationMatches(Location[] locations, int genderParam) {
        // SELECT * FROM entry WHERE locations LIKE '%817d944e-0ad0-491f-b9a5-121448926097%' OR tags LIKE '%507e4f71-9d2b-465b-b932-94754a4d1992%'

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT * FROM " + DatabaseContract.Entry.TABLE_NAME
                + " WHERE " + DatabaseContract.Entry.COLUMN_NAME_GENDER + " MATCH " + genderParam);

        if(locations.length > 0) {
            sqlQuery.append(" AND (");
            for (int i = 0; i < locations.length; i++) {
                // here I construct the query arguments based on the arguments
                if (i > 0) sqlQuery.append(" OR ");
                sqlQuery.append(DatabaseContract.Entry.COLUMN_NAME_LOCATION + " LIKE '%" + locations[i].getId().toString() + "%'");
            }
        }

        sqlQuery.append(");");

        Log.d(TAG, "getLocationMatches: query: " + sqlQuery.toString());

        DatabaseHelper mDatabaseOpenHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery.toString(), null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) { // cursor.moveToFirst() return false if cursor is empty
            cursor.close();
            return null;
        }
        db.close();
        return new ContactCursorWraper(cursor, null);
    }


    public ContactCursorWraper queryContactsTable(String selection, String[] selectionArgs) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.Entry.TABLE_NAME);
        DatabaseHelper mDatabaseOpenHelper = new DatabaseHelper(mContext);

        SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();

        Cursor cursor = builder.query(db, null, selection, selectionArgs, null, null, DatabaseContract.Entry.COLUMN_NAME_FULL_NAME);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) { // cursor.moveToFirst() return false if cursor is empty
            cursor.close();
            return null;
        }

        db.close();
        return new ContactCursorWraper(cursor, mContext);
    }


    public ContactCursorWraper queryTagsTable(String selection, String[] selectionArgs) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.TagEntry.TABLE_NAME);
        DatabaseHelper mDatabaseOpenHelper = new DatabaseHelper(mContext);

        SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
        Cursor cursor = builder.query(db, null, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) { // cursor.moveToFirst() return false if cursor is empty
            cursor.close();
            return null;
        }

        db.close();
        return new ContactCursorWraper(cursor, mContext);
    }

    public ContactCursorWraper queryLocationsTable(String selection, String[] selectionArgs) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.LocationEntry.TABLE_NAME);
        DatabaseHelper mDatabaseOpenHelper = new DatabaseHelper(mContext);

        SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();

        Cursor cursor = builder.query(db, null, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) { // cursor.moveToFirst() return false if cursor is empty
            cursor.close();
            return null;
        }

        db.close();
        return new ContactCursorWraper(cursor, mContext);
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        ContactCursorWraper cursor = queryContactsTable(null, null); // this might return null
        if (cursor == null) return contacts;
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return contacts;
    }

    public List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        ContactCursorWraper cursor = queryTagsTable(null, null);
        if (cursor == null) return tags;
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tags.add(cursor.getTag());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return tags;
    }

    public Contact getContact(UUID id) {
        ContactCursorWraper cursor = queryContactsTable(DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID + " = ?",
                new String[] {id.toString()});

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) { // cursor.moveToFirst() return false if cursor is empty
            cursor.close();
            return null;
        }
        return cursor.getContact();

    }

    public Tag getTag(UUID id) {
        ContactCursorWraper cursor = queryTagsTable(DatabaseContract.TagEntry.COLUMN_NAME_ENTRY_UUID + " = ?",
                new String[] {id.toString()});

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) { // cursor.moveToFirst() return false if cursor is empty
            cursor.close();
            return null;
        }

        return cursor.getTag();

    }


    public Location getLocation(UUID id) {
        ContactCursorWraper cursor = queryLocationsTable(DatabaseContract.TagEntry.COLUMN_NAME_ENTRY_UUID + " = ?",
                new String[] {id.toString()});

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) { // cursor.moveToFirst() return false if cursor is empty
            cursor.close();
            return null;
        }

        return cursor.getLocation();

    }

    public List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();
        ContactCursorWraper cursor = queryLocationsTable(null, null);
        if (cursor == null) return locations;
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                locations.add(cursor.getLocation());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return locations;
    }


    private static ContentValues getContentValues(Contact contact) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID, contact.getId().toString());
        values.put(DatabaseContract.Entry.COLUMN_NAME_FULL_NAME, contact.getName());
        values.put(DatabaseContract.Entry.COLUMN_NAME_FIRST_NAME, contact.getFirstName());
        values.put(DatabaseContract.Entry.COLUMN_NAME_LAST_NAME, contact.getLastName());
        values.put(DatabaseContract.Entry.COLUMN_NAME_GENDER, contact.getGender());
        values.put(DatabaseContract.Entry.COLUMN_NAME_BIRTHTIME, contact.getBirthYear());
        values.put(DatabaseContract.Entry.COLUMN_NAME_IMAGE_RESOURCE, contact.getPicturePath());
        values.put(DatabaseContract.Entry.COLUMN_NAME_NOTES, contact.getNotes());
        values.put(DatabaseContract.Entry.COLUMN_NAME_PHONE, contact.getPhone());
        values.put(DatabaseContract.Entry.COLUMN_NAME_EMAIL, contact.getEmail());
        values.put(DatabaseContract.Entry.COLUMN_NAME_LOCATION, convertArrayToString(contact.getLocationsIdArray()));
        values.put(DatabaseContract.Entry.COLUMN_NAME_TAGS, convertArrayToString(contact.getTagsIdArray()));
        values.put(DatabaseContract.Entry.COLUMN_NAME_DATE_ADED, contact.getDate().toString());
//        values.put(DatabaseContract.Entry.COLUMN_NAME_PREV_DATES, content); // same as location and mTags but with UUIDs

        return values;

    }

    private static ContentValues getContentValues(Tag tag) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TagEntry.COLUMN_NAME_ENTRY_UUID, tag.getId().toString());
        values.put(DatabaseContract.TagEntry.COLUMN_NAME_NAME, tag.getName());

        return values;

    }

    private static ContentValues getContentValues(Location location) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.LocationEntry.COLUMN_NAME_ENTRY_UUID, location.getId().toString());
        values.put(DatabaseContract.LocationEntry.COLUMN_NAME_NAME, location.getName());

        return values;

    }

    public void deleteContact(Contact contact) {
        String uuidString = contact.getId().toString();

        // Define 'where' part of query.
        String selection = DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID + " LIKE ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(uuidString) };

        // Issue SQL statement.
        mDatabase.delete(DatabaseContract.Entry.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteTag(UUID id) {
        // Define 'where' part of query.
        String selection = DatabaseContract.TagEntry.COLUMN_NAME_ENTRY_UUID + " LIKE ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };

        // Issue SQL statement.
        mDatabase.delete(DatabaseContract.TagEntry.TABLE_NAME, selection, selectionArgs);
    }

    public File getPhotoFile(Contact contact) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) return null;

        return new File(externalFilesDir, contact.getPhotoFilenane());
    }

    private static String separator = " , ";

    public static String convertArrayToString(String[] array){

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i]);

            // Do not append separator at the end of last element
            if (i < array.length - 1)
                stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    public static String[] convertStringToArray(String str){
        if (str == null || str.isEmpty()) return null;
        String[] arr = str.split(separator);
        return arr;
    }

    public static String[] removeNullValues(String[] arrayWithsNulls) {

        if (arrayWithsNulls == null) return null;

        List<String> idList = new ArrayList<>();

        for (int i = 0; i < arrayWithsNulls.length; i++) {
            if (arrayWithsNulls[i] != null) idList.add(arrayWithsNulls[i]);
        }

        if (idList.isEmpty()) return null;

        return idList.toArray(new String[idList.size()]);
    }



    public static class Filter implements Serializable {

        UUID mUUID;
        String name;

        public Filter(UUID UUID) {
            mUUID = UUID;
        }

        public Filter(String name) {
            this();
            setName(name);
        }

        public Filter() {
            this.mUUID = UUID.randomUUID();
        }

        public void setName(String name) {

            // Capitalize
            StringBuilder stringBuilder = new StringBuilder();

            String[] strArr = name.split(" ");

            for (String str : strArr) {
                char[] stringArray = str.trim().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);
                stringBuilder.append(str).append(" ");
            }

            this.name = stringBuilder.toString().trim();
        }

        public String getName() {
            return name;
        }

        public UUID getId() {
            return mUUID;
        }
    }

    public static class Tag extends Filter {
        public Tag(UUID UUID) {
            super(UUID);
        }

        public Tag(String name) {
            super(name);
        }

        public Tag() {
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class Location extends Filter {
        public Location(UUID UUID) {
            super(UUID);
        }

        public Location(String name) {
            super(name);
        }
    }

}
