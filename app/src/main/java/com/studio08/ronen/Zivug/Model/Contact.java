package com.studio08.ronen.Zivug.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ronen on 6/8/16.
 */
public class Contact {

    // Arg Gender Values
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int NOT_SET = 2;
    private static final String TAG = "Contact";

    private UUID mId;
    private Date mDate;
    private String mName, mFirstName, mLastName, mNotes, mPhone, mEmail, mPicturePath;
    private int mBirthYear;
    private int mGender;
    private Set<ContactLab.Tag> mTags;
    private Set<ContactLab.Location> mLocations;
    private Set<UUID> mPreviousDates;

    private static Random r;

    public Contact() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Contact(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public String getName() {
        return mName;
    }

    public int getGender() {
        return mGender;
    }

    public UUID getId() {
        return mId;
    }

    public int getBirthYear() {
        return mBirthYear;
    }

    public void setBirthYear(int birthYear) {
        mBirthYear = birthYear;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public int getAge() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) - mBirthYear;
    }

    public void setAge(int age) {
        // save the approximate birthday from the set age
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        this.mBirthYear =  c.get(Calendar.YEAR) - age;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public void setName(String name) {

        // Capitalize
        StringBuffer res = new StringBuffer();

        String[] strArr = name.split(" ");

//        for (String str : strArr) {
//            char[] stringArray = str.trim().toCharArray();
//            stringArray[0] = Character.toUpperCase(stringArray[0]);
//            str = new String(stringArray);
//            res.append(str).append(" ");
//        }

        for (int i = 0; i < strArr.length; i++) {
            char[] stringArray = strArr[i].trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            strArr[i] = new String(stringArray);
            if (i == 0) {
                setFirstName(strArr[0]);
            } else {
                res.append(strArr[i]).append(" ");
            }
        }

        setLastName(res.toString().trim());

        mName = mFirstName + " " + mLastName;
    }

    public void setGender(int gender) {
        mGender = gender;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPhotoFilenane() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public void setPicturePath(String picturePath) {
        this.mPicturePath = picturePath;
    }

    public String getPicturePath() {
        if (mPicturePath == null) return "";
        return mPicturePath;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "mId=" + mId +
                ", mDate=" + mDate +
                ", mName='" + mName + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mNotes='" + mNotes + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mPicturePath='" + mPicturePath + '\'' +
                ", mBirthYear=" + mBirthYear +
                ", mGender=" + mGender +
                ", mTags=" + mTags +
                ", mLocations=" + mLocations +
                ", mDate=" + mDate +
                '}';
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }


    // Tags and Location operations
    public Set<ContactLab.Tag> getTags() {
        return mTags;
    }

    public String[] getTagsIdArray() {
        if (mTags == null) return new String[0];
        List<ContactLab.Tag> tagList = new ArrayList<>(mTags.size());
        tagList.addAll(mTags);

        String[] idArray = new String[tagList.size()];

        ContactLab.Tag t;
        for (int i = 0; i < tagList.size(); i++) {
            t = tagList.get(i);
            if (t == null) idArray[i] = null;
            else idArray[i] = t.getId().toString();
        }

        return idArray;
    }

    public void setTags(Set<ContactLab.Tag> tags) {
        this.mTags = tags;
    }

    public Set<ContactLab.Location> getLocations() {
        return mLocations;
    }

    public void setLocations(Set<ContactLab.Location> locations) {
        this.mLocations = locations;
    }

    public String[] getLocationsIdArray() {
        if (mLocations == null) return new String[0];
        List<ContactLab.Location> locationList = new ArrayList<>(mLocations.size());
        locationList.addAll(mLocations);

        String[] idArray = new String[locationList.size()];

        for (int i = 0; i < locationList.size(); i++) {
            idArray[i] = locationList.get(i).getId().toString();
        }

        return idArray;
    }


    public void addTag(ContactLab.Tag tag) {
        if (mTags == null) mTags = new HashSet<>();
        this.mTags.add(tag);
    }


    public void addLocation(ContactLab.Location location) {
        this.mLocations.add(location);
    }

    public void removeTag(String tag) {
        this.mTags.remove(tag);
    }


    public void removeLocation(String location) {
        this.mLocations.remove(location);
    }

    public Date getDate() {
        return mDate;
    }

    public String[] getPreviousDatesStringArray() {
        return null;
    }

    public void setDates(Set<UUID> dates) {
        mPreviousDates = dates;
    }

    public String getPreviousDatesAsString() {
        return "";
    }

    public Set<UUID> getPreviousDates() {
        return mPreviousDates;
    }

    public boolean addPreviousDate(Contact date) {
        return mPreviousDates.add(date.getId());
    }

    public void deleteTag(ContactLab.Tag tag) {
        mTags.remove(tag);
    }
}
