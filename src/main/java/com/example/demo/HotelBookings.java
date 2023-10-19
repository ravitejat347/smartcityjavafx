package com.example.demo;
/**
 * Author: Rahul
 */

public class HotelBookings {
    private int hotelBookingId;
    private int hotelId;
    private int userId;
    private int accountId;
    private String hotelNamee;
    private String checkInTime;
    private String checkOutTime;
    private int hotelTotalCost;

    public int getHotelBookingId() {
        return hotelBookingId;
    }

    public void setHotelBookingId(int hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getHotelNamee() {
        return hotelNamee;
    }

    public void setHotelNamee(String hotelNamee) {
        this.hotelNamee = hotelNamee;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public int getHotelTotalCost() {
        return hotelTotalCost;
    }

    public void setHotelTotalCost(int hotelTotalCost) {
        this.hotelTotalCost = hotelTotalCost;
    }

    public String getHotelLocationn() {
        return hotelLocationn;
    }

    public void setHotelLocationn(String hotelLocationn) {
        this.hotelLocationn = hotelLocationn;
    }

    private String hotelLocationn;

    public HotelBookings(int hotelBookingId, String hotelNamee, String hotelLocationn, String checkInTime, String checkOutTime, int hotelTotalCost) {
        this.hotelBookingId = hotelBookingId;
        this.hotelNamee = hotelNamee;
        this.hotelLocationn = hotelLocationn;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.hotelTotalCost = hotelTotalCost;
    }

/*public HotelBookings(int hotel_booking_id, int account_id, String check_in_time, String check_out_time, int hotel_total_cost) {
            this.hotel_booking_id = hotel_booking_id;
            this.account_id = account_id;
            this.check_in_time = check_in_time;
            this.check_out_time = check_out_time;
            this.hotel_total_cost = hotel_total_cost;
        }*/



   // @Override
   // public String toString() {
     //   return hotel_booking_id + " "+account_id+" "+check_in_time+" "+check_out_time+" "+hotel_total_cost;
    //}
}
