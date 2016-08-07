package com.studio08.ronen.Zivug;

import java.util.UUID;

/**
 * Created by Ronen on 6/8/16.
 */
public class Contact {
    private UUID mId;
    private String firstName, lastName;
    private int age;
    private int resourceId;

    public Contact(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

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

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
