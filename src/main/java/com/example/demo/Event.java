package com.example.demo;
/**
 * Author: Ravi
 */

/**
 * Represents an Event with event details.
 */
public class Event extends EventBooking {

    // Fields to store event information
    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private double eventPrice;

    // Default constructor
    public Event() {
        // Default constructor with no arguments
    }

    /**
     * Constructor for an event with full details including eventId.
     *
     * @param eventId      The unique identifier for the event.
     * @param eventName    The name of the event.
     * @param eventLocation The location where the event takes place.
     * @param eventDate    The date of the event.
     * @param eventPrice   The price or cost of attending the event.
     */
    public Event(int eventId, String eventName,String eventLocation, String eventDate, double eventPrice) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventPrice = eventPrice;
    }

    /**
     * Constructor for an event without an eventId (used when creating a new event).
     *
     * @param eventName    The name of the event.
     * @param eventLocation The location where the event takes place.
     * @param eventDate    The date of the event.
     * @param eventPrice   The price or cost of attending the event.
     */
    public Event(String eventName, String eventLocation, String eventDate, double eventPrice) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventPrice = eventPrice;
    }

    // Setter method for eventId
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    // Setter method for eventName
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    // Setter method for eventDate
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    // Setter method for eventLocation
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    // Setter method for eventPrice
    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    // Getter method for eventId
    public int getEventId() {
        return eventId;
    }

    // Getter method for eventName
    public String getEventName() {
        return eventName;
    }

    // Getter method for eventDate
    public String getEventDate() {
        return eventDate;
    }

    // Getter method for eventLocation
    public String getEventLocation() {
        return eventLocation;
    }

    // Getter method for eventPrice
    public double getEventPrice() {
        return eventPrice;
    }
}

