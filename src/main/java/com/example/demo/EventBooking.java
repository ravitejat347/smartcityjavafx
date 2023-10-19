package com.example.demo;
/**
 * Author: Ravi
 */

/**
 * Represents an Event Booking with booking details.
 */
public class EventBooking {

    // Fields to store event booking information
    private int event_booking_id;
    private int event_id;
    private int user_id;
    private int account_id;
    private double event_price;

    // Default constructor
    public EventBooking(){
        // Default constructor with no arguments
    }

    /**
     * Constructor for an event booking with full details including event_booking_id.
     *
     * @param event_booking_id The unique identifier for the event booking.
     * @param event_id         The identifier for the associated event.
     * @param user_id          The user identifier associated with the booking.
     * @param account_id       The account identifier associated with the booking.
     * @param event_price      The price or cost of the event booking.
     */
    public EventBooking(int event_booking_id, int event_id, int user_id, int account_id, double event_price) {
        this.event_booking_id = event_booking_id;
        this.event_id = event_id;
        this.user_id = user_id;
        this.account_id = account_id;
        this.event_price = event_price;
    }

    /**
     * Constructor for an event booking without an event_booking_id (used when creating a new event booking).
     *
     * @param eventId    The identifier for the associated event.
     * @param userId     The user identifier associated with the booking.
     * @param accountId  The account identifier associated with the booking.
     * @param eventPrice The price or cost of the event booking.
     */
    public EventBooking(int eventId, int userId, int accountId, double eventPrice) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.account_id = account_id;
        this.event_price = event_price;
    }

    // Getter method for event_booking_id
    public int getEvent_booking_id() {
        return event_booking_id;
    }

    // Setter method for event_booking_id
    public void setEvent_booking_id(int event_booking_id) {
        this.event_booking_id = event_booking_id;
    }

    // Getter method for event_id
    public int getEvent_id() {
        return event_id;
    }

    // Setter method for event_id
    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    // Getter method for user_id
    public int getUser_id() {
        return user_id;
    }

    // Setter method for user_id
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    // Getter method for account_id
    public int getAccount_id() {
        return account_id;
    }

    // Setter method for account_id
    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    // Getter method for event_price
    public double getEvent_price() {
        return event_price;
    }

    // Setter method for event_price
    public void setEvent_cost(int event_cost) {
        this.event_price = event_price;
    }
}
