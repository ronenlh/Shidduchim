package com.studio08.ronen.Zivug.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 */

public class ContactCursorWraper extends CursorWrapper {

    // need context for the ContactLab instance
    Context mContext;

//    public ContactCursorWraper(Cursor cursor) {
//        super(cursor);
//    }

    public ContactCursorWraper(Cursor cursor, Context context) {
        super(cursor);
        this.mContext = context;
    }

    public Contact getContact() {
        String uuidString =     getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_ENTRY_UUID));
        String name =           getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_FULL_NAME));
        String firstName =      getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_FIRST_NAME));
        String lastName =       getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_LAST_NAME));
        int gender =            getInt(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_GENDER));
        int age =               getInt(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_AGE));
        String notes =          getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_NOTES));
        String picturePath =    getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_IMAGE_RESOURCE));
        String eMail =          getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_EMAIL));
        String phoneNumber =    getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_PHONE));
        String location =       getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_LOCATION));
        String mTagsString =    getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_TAGS));
//        String dates =        getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_PREV_DATES));

        Contact contact = new Contact(UUID.fromString(uuidString));
        contact.setName(name);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setGender(gender);
        contact.setAge(age);
        contact.setNotes(notes);
        contact.setPicturePath(picturePath);
        contact.setEmail(eMail);
        contact.setPhone(phoneNumber);

        String[] tagsArray = ContactLab.convertStringToArray(mTagsString);

        if (tagsArray != null) {
            Set<ContactLab.Tag> tagSet = new HashSet<>();
            ContactLab contactLab = ContactLab.get(mContext);

            for (int i = 0; i < tagsArray.length; i++) {
                ContactLab.Tag tag = contactLab.getTag(UUID.fromString(tagsArray[i]));
                tagSet.add(tag);
            }
            contact.setTags(tagSet);
        }

        return contact;
    }

    public ContactLab.Tag getTag() {
        String uuidString = getString(getColumnIndexOrThrow(DatabaseContract.TagEntry.COLUMN_NAME_ENTRY_UUID));
        String name = getString(getColumnIndexOrThrow(DatabaseContract.TagEntry.COLUMN_NAME_NAME));

        ContactLab.Tag tag = new ContactLab.Tag(UUID.fromString(uuidString));
        tag.setName(name);

        return tag;
    }
}
