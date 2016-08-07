package com.studio08.ronen.Zivug;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ronen on 7/8/16.
 */

public class ContactLab {
    private static ContactLab sContactLab;
    private List<Contact> mMaleContacts;
    private List<Contact> mFemaleContacts;

    public static ContactLab get(Context context) {
        if (sContactLab == null) {
            sContactLab = new ContactLab(context);
        }
        return sContactLab;
    }

    private ContactLab(Context context) {
        mMaleContacts = new ArrayList<>();
        mFemaleContacts = new ArrayList<>();
        initSampleDataset();
    }

    private void initSampleDataset() {
        for (int i = 1; i < 21; i++) {
            Contact contact = new Contact("Male Contact", "" + i, 12, Contact.MALE);
            mMaleContacts.add(contact);
        }
        for (int i = 1; i < 21; i++) {
            Contact contact = new Contact("Female Contact", "" + i, 25, Contact.FEMALE);
            mFemaleContacts.add(contact);
        }
    }

    public List<Contact> getMaleContacts() {
        return mMaleContacts;
    }

    public List<Contact> getFemaleContacts() {
        return mFemaleContacts;
    }

    public Contact getContact(UUID id) {
        for (Contact contact : mMaleContacts)
            if (contact.getId().equals(id))
                return contact;
        for (Contact contact : mFemaleContacts)
            if (contact.getId().equals(id))
                return contact;
        return null;
    }
}
