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
    private String mName, mNotes;
    private int mAge, mGender;
    private int mResourceId = 0;
    private int fillerResourceId;

    private static Random r;

    public Contact(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public Contact(String name, int gender, int age, int image, String notes, String location, String tags, String dates) {
        this.mName = name;
        this.mGender = gender;
        this.mAge = age;
        this.mResourceId = getFillerResourceId(gender); // for now
        this.mId = UUID.randomUUID();
        this.mNotes = notes;
        this.fillerResourceId = getFillerResourceId(gender);
    }

    public String getName() {
        return mName;
    }

    public int getGender() {
        return mGender;
    }

    public static int getFillerResourceId(int gender) {

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
    }

    public void setResourceId(int resourceId) {
        mResourceId = resourceId;
    }
}
