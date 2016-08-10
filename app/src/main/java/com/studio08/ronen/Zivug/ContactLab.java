package com.studio08.ronen.Zivug;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 */

public class ContactLab {
    private static ContactLab mContactLab;

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
        this.mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    public void addContact(Contact contact) {
        ContentValues values = getContentValues(contact);
        mDatabase.insert(DatabaseContract.Entry.TABLE_NAME,
                DatabaseContract.Entry.COLUMN_NAME_NULLABLE,
                values);
    }

    public void updateContact(Contact contact) {
        String uuidString = contact.getId().toString();
        ContentValues values = getContentValues(contact);
        mDatabase.update(DatabaseContract.Entry.TABLE_NAME,
                values, DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID + " = ?", // this is a WHERE clause, we use ? to avoid SQL injection from the literal string
                new String[] {uuidString});
    }

    public ContactCursorWraper queryContacts(String whereClause, String[] whereArgs) {

        String sortOrder = DatabaseContract.Entry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = mDatabase.query(
                DatabaseContract.Entry.TABLE_NAME,
                null, // null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                sortOrder // orderBy
        );

        return new ContactCursorWraper(cursor);
    }

//    public ContactCursorWraper searchContacts(String query) {
//
//        String rawQuery = "SELECT * FROM " + DatabaseContract.Entry.TABLE_NAME
//                + " WHERE " + DatabaseContract.Entry.COLUMN_NAME_NAME + " MATCH '" + query + "*'";
//
//        Cursor cursor = mDatabase.rawQuery(rawQuery, null);
//
//        return new ContactCursorWraper(cursor);
//    }

    public ContactCursorWraper getWordMatches(String query, String[] columns) {
        String selection = DatabaseContract.Entry.COLUMN_NAME_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private ContactCursorWraper query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.Entry.TABLE_NAME);
        DatabaseHelper mDatabaseOpenHelper = new DatabaseHelper(mContext);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return new ContactCursorWraper(cursor);
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        ContactCursorWraper cursor = queryContacts(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return new ArrayList<>();
    }

    public Contact getContact(UUID id) {
        ContactCursorWraper cursor = queryContacts(DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID + " = ?",
                new String[] {id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getContact();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Contact contact) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID, contact.getId().toString());
        values.put(DatabaseContract.Entry.COLUMN_NAME_NAME, contact.getName());
        values.put(DatabaseContract.Entry.COLUMN_NAME_GENDER, contact.getGender());
        values.put(DatabaseContract.Entry.COLUMN_NAME_AGE, contact.getAge());
        values.put(DatabaseContract.Entry.COLUMN_NAME_IMAGE_RESOURCE, contact.getResourceId());
        values.put(DatabaseContract.Entry.COLUMN_NAME_NOTES, contact.getNotes());
//        values.put(DatabaseContract.Entry.COLUMN_NAME_LOCATION, content);
//        values.put(DatabaseContract.Entry.COLUMN_NAME_TAGS, content);
//        values.put(DatabaseContract.Entry.COLUMN_NAME_PREV_DATES, content);

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

}
