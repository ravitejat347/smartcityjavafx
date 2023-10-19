package com.example.demo;
/**
 * Author: Owen
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * User
 * Author: Owen Wurster
 */
public class User {

    private static User user = null;

    final private int userID;
    private final int roleID;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String zipcode;
    private String state;
    private String email;
    private String phoneNumber;


    public User(int userID, String firstName,String lastName, String emailAddress, int roleID){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = emailAddress;
        this.roleID = roleID;
    }

    /**
     * private User constructor
     * Only called in initializeUser when user is null
     *
     * @param userID user ID
     * @param firstName first name
     * @param lastName last name
     * @param streetAddress street address
     * @param city city
     * @param zipcode zipcode
     * @param state state
     * @param email email
     * @param phoneNumber phone number
     * @param roleID role ID
     */
    private User(int userID,
                 String firstName,
                 String lastName,
                 String streetAddress,
                 String city,
                 String zipcode,
                 String state,
                 String email,
                 String phoneNumber,
                 int roleID) {

        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
    }

    /**
     * initializeUser
     * Used to initialize user if the object is null
     *
     * @param userID user ID
     * @param firstName first name
     * @param lastName last name
     * @param streetAddress street address
     * @param city city
     * @param zipcode zipcode
     * @param state state
     * @param email email
     * @param phoneNumber phone number
     * @param roleID role ID
     */
    public static synchronized void initializeUser(int userID,
                                                   String firstName,
                                                   String lastName,
                                                   String streetAddress,
                                                   String city,
                                                   String zipcode,
                                                   String state,
                                                   String email,
                                                   String phoneNumber,
                                                   int roleID) {
        if (user == null)
            user = new User(userID,
                    firstName,
                    lastName,
                    streetAddress,
                    city,
                    zipcode,
                    state,
                    email,
                    phoneNumber,
                    roleID);

    }

    /**
     * logOut
     * Sets the user object to null when the user logs out
     */
    public static void logOut() {
        user = null;
    }

    /**
     * getInstance
     * Returns a user object to be interacted with by the rest of the application
     *
     * @return a user object
     */
    public static synchronized User getInstance() {
        return user;
    }



    // Getters

    /**
     * getUserID
     * Returns the user's ID
     * @return int userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * getFirstName
     * Returns the user's first name
     * @return String firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * getLastName
     * Returns the user's last name
     * @return String lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * getStreetAddress
     * Returns the user's street address
     * @return String streetAddress
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * getCity
     * Returns the city that the user lives in
     * @return String city
     */
    public String getCity() {
        return city;
    }

    /**
     * getZipcode
     * Return's the user's zipcode
     * @return String zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * getState
     * Returns the state that the user lives in
     * @return String state
     */
    public String getState() {
        return state;
    }

    /**
     * getEmail
     * Returns the user's email
     * @return String email
     */
    public String getEmail() {
        return email;
    }

    /**
     * getPhoneNumber
     * Returns the user's phone number
     * @return String phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * getRoleID
     * Returns the user's role
     *
     * If roleID = 1: User is a User
     * if roleID = 2: User is an Admin
     * @return int roleID
     */
    public int getRoleID() {
        return roleID;
    }

    // Setters

    /**
     * setFirstName
     * Sets the user's first name
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * setLastName
     * Sets the user's last name
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * setStreetAddress
     * Sets the user's street address
     * @param address street address
     */
    public void setStreetAddress(String address) {
        this.streetAddress = address;
    }

    /**
     * setCity
     * Sets the city that the user lives in
     * @param city city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * setZipcode
     * Sets the user's zipcode
     * @param zipcode zipcode
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * setState
     * Sets the state that the user lives in
     * @param state state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * setEmail
     * Sets the user's email
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * setPhoneNumber
     * Sets the user's phone number
     * @param phoneNumber phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * toString
     * @return String address
     */
    public String toString() {
        String address;
        address = this.streetAddress + " " + this.city + ",\n" + this.state + " " + this.zipcode;
        return address;
    }

    /**
     * firstNameProperty
     * @return SimpleStringProperty firstName
     */
    public StringProperty firstNameProperty(){
        return new SimpleStringProperty(firstName);
    }

    /**
     * lastNameProperty
     * @return SimpleStringProperty lastName
     */
    public StringProperty lastNameProperty(){
        return new SimpleStringProperty(lastName);
    }

    /**
     * emailProperty
     * @return StringProperty email
     */
    public StringProperty emailProperty(){
        return new SimpleStringProperty(email);
    }

    /**
     * adminProperty
     * @return StringProperty admin
     */
    public StringProperty adminProperty(){
        String admin;
        if(roleID == 1){
            admin = "No";
        }
        else{
            admin = "Yes";
        }
        return new SimpleStringProperty(admin);
    }
}
