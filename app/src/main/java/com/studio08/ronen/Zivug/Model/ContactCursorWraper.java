package com.studio08.ronen.Zivug.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.HashSet;
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
        String mlocationString =getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_LOCATION));
        String mTagsString =    getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_TAGS));
        String mdatesString =   getString(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_PREV_DATES));

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
        String[] locationArray = ContactLab.convertStringToArray(mlocationString);
        String[] dateArray = ContactLab.convertStringToArray(mdatesString);

        if (tagsArray != null) {
            Set<ContactLab.Tag> tagSet = new HashSet<>();
            ContactLab contactLab = ContactLab.get(mContext);

            for (int i = 0; i < tagsArray.length; i++) {
                ContactLab.Tag t = contactLab.getTag(UUID.fromString(tagsArray[i]));
                tagSet.add(t);
            }
            contact.setTags(tagSet);
        }

        if (locationArray != null) {
            Set<ContactLab.Location> locationSet = new HashSet<>();
            ContactLab contactLab = ContactLab.get(mContext);

            for (int i = 0; i < locationArray.length; i++) {
                ContactLab.Location l = contactLab.getLocation(UUID.fromString(locationArray[i]));
                locationSet.add(l);
            }
            contact.setLocations(locationSet);
        }

        if (dateArray != null) {
            Set<UUID> datesSet = new HashSet<>();
            ContactLab contactLab = ContactLab.get(mContext);

            for (int i = 0; i < dateArray.length; i++) {
                UUID d = UUID.fromString(dateArray[i]);
                datesSet.add(d);
            }
            contact.setDates(datesSet);
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

    public ContactLab.Location getLocation() {
        String uuidString = getString(getColumnIndexOrThrow(DatabaseContract.LocationEntry.COLUMN_NAME_ENTRY_UUID));
        String name = getString(getColumnIndexOrThrow(DatabaseContract.LocationEntry.COLUMN_NAME_NAME));

        ContactLab.Location location = new ContactLab.Location(UUID.fromString(uuidString));
        location.setName(name);

        return location;
    }
}
