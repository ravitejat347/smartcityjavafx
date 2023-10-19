package com.example.demo;

/**
 * Author: Maaz
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a bus with a short name, long name, route ID, and route main ID.
 */
class Bus {
    StringProperty shortName;
    StringProperty longName;
    StringProperty routeID;
    String routeMainId;

    /**
     * Constructs a Bus object with the specified short name, long name, route ID, and route main ID.
     *
     * @param shortName   The short name of the bus.
     * @param longName    The long name of the bus.
     * @param routeID     The route ID of the bus.
     * @param routeMainId The main route ID of the bus.
     */
    public Bus(String shortName, String longName, String routeID, String routeMainId) {
        this.shortName = new SimpleStringProperty(shortName);
        this.longName = new SimpleStringProperty(longName);
        this.routeID = new SimpleStringProperty(routeID);
        this.routeMainId = routeMainId;
    }

    /**
     * Gets the short name of the bus.
     *
     * @return The short name of the bus.
     */
    public StringProperty getShortName() {
        return shortName;
    }

    /**
     * Gets the long name of the bus.
     *
     * @return The long name of the bus.
     */
    public StringProperty getLongName() {
        return longName;
    }

    /**
     * Gets the route ID of the bus.
     *
     * @return The route ID of the bus.
     */
    public StringProperty getRouteID() {
        return routeID;
    }

    /**
     * Gets the route main ID of the bus.
     *
     * @return The route main ID of the bus.
     */
    public String getRouteMainId() {
        return routeMainId;
    }
}
/**
 * Author: Maaz
 */

/**
 * Represents a specific point in time when a bus stops at a station.
 */
class Stop {
    SimpleStringProperty stopName;
    SimpleStringProperty arrivalTime;
    SimpleStringProperty departureTime;

    /**
     * Constructs a Stop object with the specified stop name, arrival time, and departure time.
     *
     * @param stopName      The name of the bus stop.
     * @param arrivalTime   The arrival time of the bus at the stop.
     * @param departureTime The departure time of the bus from the stop.
     */
    public Stop(SimpleStringProperty stopName, SimpleStringProperty arrivalTime, SimpleStringProperty departureTime) {
        this.stopName = stopName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    /**
     * Gets the property representing the stop name.
     *
     * @return The property representing the stop name.
     */
    public SimpleStringProperty stopNameProperty() {
        return stopName;
    }

    /**
     * Gets the property representing the arrival time.
     *
     * @return The property representing the arrival time.
     */
    public SimpleStringProperty arrivalTimeProperty() {
        return arrivalTime;
    }

    /**
     * Gets the property representing the departure time.
     *
     * @return The property representing the departure time.
     */
    public SimpleStringProperty departureTimeProperty() {
        return departureTime;
    }
}