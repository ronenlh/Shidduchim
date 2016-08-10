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
    private int mResourceId = 0;
    private int fillerResourceId;

    public boolean isImageSet = false; // maybe set a condition on the adapter to show the filler image, but right now there are many and associated with each contact

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

    // avatars
    public static final int[] MALE_AVATARS = {R.drawable.avatar_01, R.drawable.avatar_02,
            R.drawable.avatar_03, R.drawable.avatar_04,
            R.drawable.avatar_11, R.drawable.avatar_12,
            R.drawable.avatar_13, R.drawable.avatar_14};
    public static final int[] FEMALE_AVATARS = {R.drawable.avatar_16, R.drawable.avatar_17,
            R.drawable.avatar_18, R.drawable.avatar_19,
            R.drawable.avatar_21, R.drawable.avatar_22,
            R.drawable.avatar_24};



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

    public int getFillerResourceId() {
        return fillerResourceId;
    }

    private void setFillerResourceId(int gender) {
        if (r == null) r = new Random();

        if (gender == MALE) {
            int randomIndex = r.nextInt(MALE_AVATARS.length);
            fillerResourceId = MALE_AVATARS[randomIndex];
        } else if (gender == FEMALE) {
            int randomIndex = r.nextInt(FEMALE_AVATARS.length);
            fillerResourceId = FEMALE_AVATARS[randomIndex];
        } else {
            fillerResourceId = -1;
        }
    }

    public static int getInitialFillerResourceId(int gender) {
        if (r == null) r = new Random();

        if (gender == MALE) {
            int randomIndex = r.nextInt(MALE_AVATARS.length);
            return MALE_AVATARS[randomIndex];
        } else if (gender == FEMALE) {
            int randomIndex = r.nextInt(FEMALE_AVATARS.length);
            return FEMALE_AVATARS[randomIndex];
        }
        return -1;
    }

    public int getResourceId() {
        if (mResourceId == 0)
            return getFillerResourceId();
        else
            return mResourceId;
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
        setFillerResourceId(mGender);
    }

    public void setResourceId(int resourceId) {
        mResourceId = resourceId;
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
        mPicturePath = picturePath;
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
