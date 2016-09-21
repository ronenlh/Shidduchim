package com.studio08.ronen.Zivug.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ronen on 9/8/16.
 */

public class ContactCursorWraper extends CursorWrapper {

    private static final String TAG = "ContactCursorWraper";
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
        int mBirthTime =        getInt(getColumnIndexOrThrow(DatabaseContract.Entry.COLUMN_NAME_BIRTHTIME));
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
        contact.setBirthYear(mBirthTime);
        contact.setNotes(notes);
        contact.setPicturePath(picturePath);
        contact.setEmail(eMail);
        contact.setPhone(phoneNumber);

        String[] tagsArrayNull = ContactLab.convertStringToArray(mTagsString);
        String[] locationArrayNull = ContactLab.convertStringToArray(mlocationString);
        String[] dateArrayNull = ContactLab.convertStringToArray(mdatesString);

        String[] tagsArray = ContactLab.removeNullValues(tagsArrayNull);
        String[] locationArray = ContactLab.removeNullValues(locationArrayNull);
        String[] dateArray = ContactLab.removeNullValues(dateArrayNull);

        Log.d(TAG, "getContact: Name: " + contact.getName());
        if (tagsArray != null) {
            Set<ContactLab.Tag> tagSet = new HashSet<>();
            ContactLab contactLab = ContactLab.get(mContext);

            for (int i = 0; i < tagsArray.length; i++) {
                Log.d(TAG, "getContact: tag: " + tagsArray[i]);
                String s = tagsArray[i];
                try {
                    ContactLab.Tag t = contactLab.getTag(UUID.fromString(s));
                    tagSet.add(t);
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "getContact: ", e);
                }
            }
            contact.setTags(tagSet);
        }

        /** TODO:
         * cleanup old tags, location, and dates ids from the database in case the original was deleted,
         * I don't know where and when should I do that, but I know how
         */

        if (locationArray != null) {
            Set<ContactLab.Location> locationSet = new HashSet<>();
            ContactLab contactLab = ContactLab.get(mContext);

            for (int i = 0; i < locationArray.length; i++) {
                String s = locationArray[i];
                try {
                    ContactLab.Location l = contactLab.getLocation(UUID.fromString(s));
                    locationSet.add(l);
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "getContact: ", e);
                }
            }
            contact.setLocations(locationSet);
        }

        if (dateArray != null) {
            Set<UUID> datesSet = new HashSet<>();

            for (int i = 0; i < dateArray.length; i++) {
                String s = dateArray[i];
                try {
                    UUID d = UUID.fromString(s);
                    datesSet.add(d);
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "getContact: ", e);
                }
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
