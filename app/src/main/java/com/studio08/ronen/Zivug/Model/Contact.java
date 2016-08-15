package com.studio08.ronen.Zivug.Model;

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
    private String mName, mFirstName, mLastName, mNotes, mPhone, mEmail, mPicturePath;
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
                ", mEmail='" + mEmail + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mNotes='" + mNotes + '\'' +
                ", mPicturePath='" + mPicturePath + '\'' +
                ", mAge=" + mAge +
                ", mGender=" + mGender +
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
}
