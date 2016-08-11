package com.studio08.ronen.Zivug;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 * Class responsible for everything related to persisting data in the app
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

    public ContactCursorWraper getWordMatches(String query) {
        String selection = DatabaseContract.Entry.COLUMN_NAME_FULL_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return queryContacts(selection, selectionArgs);
    }

    public ContactCursorWraper queryContacts(String selection, String[] selectionArgs) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.Entry.TABLE_NAME);
        DatabaseHelper mDatabaseOpenHelper = new DatabaseHelper(mContext);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                null, selection, selectionArgs, null, null, null);

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
        values.put(DatabaseContract.Entry.COLUMN_NAME_FULL_NAME, contact.getName());
        values.put(DatabaseContract.Entry.COLUMN_NAME_FIRST_NAME, contact.getFirstName());
        values.put(DatabaseContract.Entry.COLUMN_NAME_LAST_NAME, contact.getLastName());
        values.put(DatabaseContract.Entry.COLUMN_NAME_GENDER, contact.getGender());
        values.put(DatabaseContract.Entry.COLUMN_NAME_AGE, contact.getAge());
        values.put(DatabaseContract.Entry.COLUMN_NAME_IMAGE_RESOURCE, contact.getPicturePath());
        values.put(DatabaseContract.Entry.COLUMN_NAME_NOTES, contact.getNotes());
        values.put(DatabaseContract.Entry.COLUMN_NAME_PHONE, contact.getPhone());
        values.put(DatabaseContract.Entry.COLUMN_NAME_EMAIL, contact.getEmail());
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

    public File getPhotoFile(Contact contact) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) return null;

        return new File(externalFilesDir, contact.getPhotoFilenane());
    }

}
