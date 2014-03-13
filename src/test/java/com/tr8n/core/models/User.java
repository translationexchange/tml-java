package com.tr8n.core.models;

/**
 * Created by michael on 3/12/14.
 */
public class User {

    private String firstName;

    private String lastName;

    private String gender;

    private String age;

    public User(String firstName, String gender) {
        setFirstName(firstName);
        setGender(gender);
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String toString() {
        return getFirstName();
    }
}
