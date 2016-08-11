package com.studio08.ronen.Zivug;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Ronen on 6/8/16.
 */
public class Contact {

    // Arg Gender Values
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int NOT_SET = 2;

    private UUID mId;
    private Date mDate;
    private String mName, mNotes, mPhone, mEmail, mPicturePath;
    private int mAge, mGender;

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


    public void setId(UUID id) {
        mId = id;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        this.mAge = age;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public void setName(String name) {
        mName = name;
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
        return mPicturePath;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "mId=" + mId +
                ", mDate=" + mDate +
                ", mName='" + mName + '\'' +
                ", mNotes='" + mNotes + '\'' +
                ", mPicturePath='" + mPicturePath + '\'' +
                ", mAge=" + mAge +
                ", mGender=" + mGender +
                '}';
    }
}
