package com.studio08.ronen.Zivug.Model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 */

public class ContactCursorWraper extends CursorWrapper {
    public ContactCursorWraper(Cursor cursor) {
        super(cursor);
    }

    public Contact getContact() {
        String uuidString = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID));
        String name = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_FULL_NAME));
        String firstName = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_FIRST_NAME));
        String lastName = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_LAST_NAME));
        int gender = getInt(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_GENDER));
        int age = getInt(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_AGE));
//        int resourceId = getInt(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_IMAGE_RESOURCE));
        String notes = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_NOTES));
        String picturePath = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_IMAGE_RESOURCE));
        String eMail = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_EMAIL));
        String phoneNumer = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_PHONE));
//        String location = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_LOCATION));
//        String mTags = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_TAGS));
//        String dates = getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_PREV_DATES));

        Contact contact = new Contact(UUID.fromString(uuidString));
        contact.setName(name);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setGender(gender);
        contact.setAge(age);
//        contact.setResourceId(resourceId);
        contact.setNotes(notes);
        contact.setPicturePath(picturePath);
        contact.setEmail(eMail);
        contact.setPhone(phoneNumer);
        return contact;
    }
}
