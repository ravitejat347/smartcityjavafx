package com.example.demo;
/**
 * Author: Rahul
 */

/**
 * The Hotel class represents a hotel entity with attributes like hotel ID, name, location, price, room number, and availability.
 */
public class Hotel {
    private int hotelId;
    private String hotelName;
    private String hotelLocation;
    private int hotelPrice;
    private int hotelRoomNumber;
    private int hotelAvailability;

    /**
     * Constructs a new Hotel object with the specified attributes.
     *
     * @param hotelId          The unique identifier of the hotel.
     * @param hotelName        The name of the hotel.
     * @param hotelLocation    The location of the hotel.
     * @param hotelPrice       The price per night at the hotel.
     * @param hotelRoomNumber  The total number of rooms available at the hotel.
     * @param hotelAvailability The availability status of the hotel.
     */
    public Hotel(int hotelId, String hotelName, String hotelLocation, int hotelPrice, int hotelRoomNumber, int hotelAvailability) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelLocation = hotelLocation;
        this.hotelPrice = hotelPrice;
        this.hotelRoomNumber = hotelRoomNumber;
        this.hotelAvailability = hotelAvailability;
    }

    /**
     * Gets the unique identifier of the hotel.
     *
     * @return The hotel ID.
     */
    public int getHotelId() {
        return hotelId;
    }

    /**
     * Gets the name of the hotel.
     *
     * @return The hotel name.
     */
    public String getHotelName() {
        return hotelName;
    }

    /**
     * Gets the location of the hotel.
     *
     * @return The hotel location.
     */
    public String getHotelLocation() {
        return hotelLocation;
    }

    /**
     * Gets the price per night at the hotel.
     *
     * @return The hotel price.
     */
    public int getHotelPrice() {
        return hotelPrice;
    }

    /**
     * Gets the total number of rooms available at the hotel.
     *
     * @return The room number.
     */
    public int getHotelRoomNumber() {
        return hotelRoomNumber;
    }

    /**
     * Gets the availability status of the hotel.
     *
     * @return The hotel availability.
     */
    public int getHotelAvailability() {
        return hotelAvailability;
    }

    /**
     * Returns a string representation of the Hotel object.
     *
     * @return A string containing the hotel name, location, price, and availability.
     */
    @Override
    public String toString() {
        return hotelName + " "+hotelLocation+" "+hotelPrice+" "+hotelAvailability;
    }
}
