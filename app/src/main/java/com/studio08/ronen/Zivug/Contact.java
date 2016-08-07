package com.studio08.ronen.Zivug;

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
    private String firstName, lastName;
    private int age, gender;
    private int resourceId = 0;
    private int fillerResourceId;

    private static Random r;

    public Contact(String firstName, String lastName, int age, int gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;


//        if (r != null)
            r = new Random();

        if (gender == MALE) {
            int randomIndex = r.nextInt(MALE_AVATARS.length);
            fillerResourceId = MALE_AVATARS[randomIndex];
        } else if (gender == FEMALE) {
            int randomIndex = r.nextInt(FEMALE_AVATARS.length);
            fillerResourceId = FEMALE_AVATARS[randomIndex];
        }
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


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFillerResourceId() {
        return fillerResourceId;
    }

    public int getResourceId() {
        if (resourceId == 0)
            return getFillerResourceId();
        else
            return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
