package com.example.demo;
/**
 * Author: Ravi
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * EventBookingController handles the interaction between the user interface and event booking functionality.
 */
public class EventBookingController {
    @FXML
    private VBox mainContentView;
    @FXML
    private VBox event1Details;
    @FXML
    private VBox event2Details;

    @FXML
    private VBox event3Details;

    @FXML
    private VBox bookingDetails;

    // TableViews for displaying event data and bookings
    @FXML
    private TableView<Event> bookingTableView;

    @FXML
    private TableView<Event> eventsTableView1;
    @FXML
    private TableView<Event> eventsTableView2;
    @FXML
    private TableView<Event> eventsTableView3;

    // TableColumn definitions
    @FXML
    private TableColumn<Event, String> eventNameColumn;

    @FXML
    private TableColumn<Event, String> eventDateColumn;

    @FXML
    private TableColumn<Event, String> eventLocationColumn;

    @FXML
    private TableColumn<Event, Double> eventPriceColumn;

    // User role and other data
    private int userRole = User.getInstance().getRoleID();
    private static int userId;
    private ObservableList<EventBooking> eventBookingData = FXCollections.observableArrayList();
    private Event selectedItem;

    // Load event booking data from the database
    private void loadDataFromDatabase() {
        // Clear existing data
        bookingTableView.getItems().clear();
        String sql = "SELECT e.event_name, e.event_date, e.event_location, e.event_price " +
                "FROM event_booking eb " +
                "INNER JOIN event e ON eb.event_id = e.event_id " +
                "WHERE eb.user_id = ?";

        try {
            int userId = getUser_Id(); // Obtain the userId

            Connection connection = DBConn.connectDB();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String eventName = resultSet.getString("event_name");
                String eventLocation = resultSet.getString("event_location");
                String eventDate = resultSet.getString("event_date");
                double eventPrice = resultSet.getDouble("event_price");

                Event event = new Event(eventName, eventLocation, eventDate, eventPrice);
                eventBookingData.add(event);
                bookingTableView.getItems().add(event);
            }

            // Close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Error", "Failed to load event bookings.");
        }
    }
    private void loadBookingsForUser(String userEmail) {
        // Query the database for bookings associated with the specified email
        // Modify your SQL query to retrieve bookings for the given user's email
        String sql = "SELECT e.event_name, e.event_date, e.event_location, e.event_price " +
                "FROM event_booking eb " +
                "INNER JOIN event e ON eb.event_id = e.event_id " +
                "INNER JOIN user u ON eb.user_id = u.uid " +
                "WHERE u.user_email = ?";

        try {
            Connection connection = DBConn.connectDB();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userEmail);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear existing data
            bookingTableView.getItems().clear();
            while (resultSet.next()) {
                String eventName = resultSet.getString("event_name");
                String eventLocation = resultSet.getString("event_location");
                String eventDate = resultSet.getString("event_date");
                double eventPrice = resultSet.getDouble("event_price");

                Event event = new Event(eventName, eventLocation, eventDate, eventPrice);
                eventBookingData.add(event);
                bookingTableView.getItems().add(event);
            }

            // Close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Error", "Failed to load event bookings for the specified user.");
        }
    }

    /**
     * Get the user ID associated with the logged-in user.
     *
     * @return The user ID of the logged-in user.
     */
    public int getUser_Id(){
        String userEmail = HotelBooking.getInstance().getEmailId();
        HotelBooking c = new HotelBooking();
        userId = c.getUserdetails(userEmail);
        return userId;
    }

    /**
     * Get the account ID associated with the user.
     *
     * @param userId The user ID for which to retrieve the account ID.
     * @return The account ID associated with the user.
     */
    public int getAccountId(int userId) {
        int accountId = -1; // Default value in case of an error

        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT account_no FROM bank_account WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                accountId = resultSet.getInt("account_no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountId;
    }

    // Handle the "View Bookings" button click event
    @FXML
    private void viewBookings() {
        if (userRole == 2) {
            // Create a dialog to prompt for the user's email
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("View User's Bookings");
            dialog.setHeaderText("Enter User's Email");
            dialog.setContentText("User Email:");

            // Show the dialog and wait for user input
            Optional<String> result = dialog.showAndWait();

            // If the user entered an email, query the database
            result.ifPresent(email -> {
                // Call a method to load bookings for the specified email
                loadBookingsForUser(email);
            });
        } else {
            // If the user is not an admin, just load the bookings without prompting for email
            loadDataFromDatabase();
        }
        mainContentView.setVisible(false);
        event1Details.setVisible(false);
        event2Details.setVisible(false);
        event3Details.setVisible(false);
        bookingDetails.setVisible(true);
    }


    // Handle the main content view when returning from details or bookings view
    @FXML
    private void mainContentView2() {
        mainContentView.setVisible(true);
        event1Details.setVisible(false);
        event2Details.setVisible(false);
        event3Details.setVisible(false);
        bookingDetails.setVisible(false);
    }

    // Handle the selection of a booking in the TableView
    @FXML
    private void handleBookingSelection(MouseEvent event) {
        selectedItem = bookingTableView.getSelectionModel().getSelectedItem();
    }

    // Handle the selection of an event in the TableView
    @FXML
    private void handleEventSelection1(MouseEvent event) {
        selectedItem = eventsTableView1.getSelectionModel().getSelectedItem();
    }

    // Create a new event booking
    @FXML
    private void createBooking() {
        // Get the user_id and account_id
        int userId = getUser_Id();
        int accountId = getAccountId(userId);

        if (userId == -1 || accountId == -1) {
            showErrorDialog("Error", "Failed to retrieve user or account information.");
            return;
        }

        // Calculate the user's current account balance
        double accountBalance = getBalance(accountId);

        if(event1Details.isVisible() == true){
            selectedItem = eventsTableView1.getSelectionModel().getSelectedItem();
        } else if (event2Details.isVisible() == true) {
            selectedItem = eventsTableView2.getSelectionModel().getSelectedItem();
        } else if (event3Details.isVisible() == true) {
            selectedItem = eventsTableView3.getSelectionModel().getSelectedItem();
        }

        if (selectedItem == null) {
            showErrorDialog("Error", "No event selected for booking.");
            return;
        }
        // Check if the event is already booked
        boolean isEventBooked = checkEventBooking(selectedItem, userId);

        if (isEventBooked) {
            showErrorDialog("Error", "This event has already been booked.");
            return;
        }

        // Check if the user has sufficient funds for the booking
        double eventPrice = selectedItem.getEventPrice();

        if (accountBalance >= eventPrice) {
            // Sufficient funds, create the booking
            boolean bookingSuccess = createEventBooking(selectedItem, userId, accountId, eventPrice);

            if (bookingSuccess) {
                // Update the account balance after deducting the amount
                double newBalance = accountBalance - eventPrice;
                updateBalance(accountId, newBalance);

                // Show a success message
                showInfoDialog("Success", "Booking successful!");
                loadDataFromDatabase();

            } else {
                showErrorDialog("Error", "Failed to create the booking.");
            }
        } else {
            // Insufficient funds, show an error message
            showErrorDialog("Error", "Insufficient funds in bank account. Booking failed.");
        }
    }

    // Check if an event is already booked by the user
    private boolean checkEventBooking(Event event, int userId) {
        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT * FROM event_booking WHERE event_id = ? AND user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, event.getEventId());
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // Returns true if the event is already booked by the user
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Assume an error occurred
        }
    }

    // Create an event booking in the database
    private boolean createEventBooking(Event selectedEvent, int userId, int accountId, double eventPrice) {
        try (Connection connection = DBConn.connectDB()) {
            String sql = "INSERT INTO event_booking (event_id, user_id, account_id, event_price) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, selectedEvent.getEventId());
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, accountId);
            preparedStatement.setDouble(4, eventPrice);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Show an information dialog
    private void showInfoDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Get the current account balance
    private double getBalance(int accountId) {
        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT balance FROM bank_account WHERE account_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
            return 0.0; // Default if no balance is found
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    // Update the account balance
    private void updateBalance(int accountId, double newBalance) {
        try (Connection connection = DBConn.connectDB()) {
            String sql = "UPDATE bank_account SET balance = ? WHERE account_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle the deletion of a booking
    @FXML
    private void deleteBooking() {
        if(bookingTableView.isVisible() == true){
            selectedItem = bookingTableView.getSelectionModel().getSelectedItem();
        }
        if (selectedItem == null) {
            showErrorDialog("Error", "No booking selected for deletion.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this booking?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed the deletion
            boolean deleted = deleteBookingFromDatabase(selectedItem.getEventName(), selectedItem.getEventDate());

            if (deleted) {
                showInfoDialog("Success", "Booking deleted successfully.");
               // loadDataFromDatabase(); // Reload the data to update the view
                viewBookings();
            } else {
                showErrorDialog("Error", "Failed to delete the booking.");
            }
        }
    }

    // Handle the deletion of a booking
    private boolean deleteBookingFromDatabase(String eventName, String eventDate) {
        if(userRole == 1){
            try (Connection connection = DBConn.connectDB()) {
                String sql = "DELETE FROM event_booking WHERE event_id = (SELECT event_id FROM event WHERE event_name = ? AND event_date = ?) AND user_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, eventName);
                preparedStatement.setString(2, eventDate);
                preparedStatement.setInt(3, getUser_Id()); // You may need to replace this with the actual user ID

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else if (userRole == 2) {
            try (Connection connection = DBConn.connectDB()) {
                String sql = "DELETE FROM event_booking WHERE event_id = (SELECT event_id FROM event WHERE event_name = ? AND event_date = ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, eventName);
                preparedStatement.setString(2, eventDate);
                // preparedStatement.setInt(3, getUser_Id()); // You may need to replace this with the actual user ID

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    // Show an error dialog
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
