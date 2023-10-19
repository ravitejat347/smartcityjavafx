package com.example.demo;
/**
 * Author: Ravi
 */

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

/**
 * EventsController handles events-related functionality and extends EventBookingController.
 */
public class EventsController extends EventBookingController{
    @FXML
    private VBox event1Details;
    @FXML
    private VBox event2Details;

    @FXML
    private VBox event3Details;
    @FXML
    private Button moreDetails1;

    @FXML
    private Button moreDetails2;

    @FXML
    private Button moreDetails3;
    @FXML
    private Button event1createEventButton;
    @FXML
    private Button event1deleteEventButton;
    @FXML
    private Button event2createEventButton;
    @FXML
    private Button event2deleteEventButton;
    @FXML
    private Button event3createEventButton;
    @FXML
    private Button event3deleteEventButton;

    @FXML
    private TableView<Event> eventsTableView1; // TableView for More Details 1

    @FXML
    private TableView<Event> eventsTableView2; // TableView for More Details 2

    @FXML
    private TableView<Event> eventsTableView3; // TableView for More Details 3
    @FXML
    private VBox mainContentView;
    private Event selectedEvent1;
    private Event selectedEvent2;
    private Event selectedEvent3;
    private int userRole = User.getInstance().getRoleID();

    // Initialize the table data
    private ObservableList<Event> eventData1 = FXCollections.observableArrayList();
    private ObservableList<Event> eventData2 = FXCollections.observableArrayList();
    private ObservableList<Event> eventData3 = FXCollections.observableArrayList();

    // Load event data from the database
    private void loadDataFromDatabase() {
       // Clear the existing data
        eventData1.clear();
        eventData2.clear();
        eventData3.clear();
        eventsTableView1.getItems().clear();
        eventsTableView2.getItems().clear();
        eventsTableView3.getItems().clear();
        String sql = "SELECT * FROM event";

        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int eventId = resultSet.getInt("event_id");
                String eventName = resultSet.getString("event_name");
                String eventLocation = resultSet.getString("event_location");
                String eventDate = resultSet.getString("event_date");
                double eventPrice = resultSet.getDouble("event_price");

                Event event = new Event(eventId, eventName, eventLocation, eventDate, eventPrice);

                // Determine which TableView to set based on the event_id range

                if (eventId >= 1000 && eventId <= 1999) {
                    eventData1.add(event);
                    eventsTableView1.getItems().add(event);
                } else if (eventId >= 2000 && eventId <= 2999) {
                    eventData2.add(event);
                    eventsTableView2.getItems().add(event);
                } else if (eventId >= 3000 && eventId <= 3999) {
                    eventData3.add(event);
                    eventsTableView3.getItems().add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the creation of a new event.
     */
    @FXML
    private void createEvent() {
        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Create Event");
        dialog.setHeaderText("Enter Event Details:");

        // Create the GridPane to layout the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Create labels and input fields for event details
        TextField eventNameField = new TextField();
        TextField eventLocationField = new TextField();
        DatePicker eventDatePicker = new DatePicker();
        TextField eventPriceField = new TextField();

        eventNameField.setPromptText("Event Name");
        eventLocationField.setPromptText("Event Location");
        eventPriceField.setPromptText("Event Price");

        // Add the components to the GridPane
        grid.add(new Label("Event Name:"), 0, 0);
        grid.add(eventNameField, 1, 0);
        grid.add(new Label("Event Location:"), 0, 1);
        grid.add(eventLocationField, 1, 1);
        grid.add(new Label("Event Date:"), 0, 2);
        grid.add(eventDatePicker, 1, 2);
        grid.add(new Label("Event Price:"), 0, 3);
        grid.add(eventPriceField, 1, 3);

        // Set the GridPane as the content of the dialog
        dialog.getDialogPane().setContent(grid);

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                Event newEvent = new Event();
                // Generate a random event_id based on which button was clicked
                int minEventId, maxEventId;
                if (event1Details.isVisible()) {
                    minEventId = 1000;
                    maxEventId = 1999;
                } else if (event2Details.isVisible()) {
                    minEventId = 2000;
                    maxEventId = 2999;
                } else if (event3Details.isVisible()) {
                    minEventId = 3000;
                    maxEventId = 3999;
                } else {
                    // Default range or error handling
                    minEventId = 1000;
                    maxEventId = 1999;
                }
                int randomEventId;
                do {
                    randomEventId = getRandomEventId(minEventId, maxEventId);
                } while (!isEventIdUnique(randomEventId));
                newEvent.setEventId(randomEventId);
                newEvent.setEventName(eventNameField.getText());
                newEvent.setEventLocation(eventLocationField.getText());
                // Get the selected date from the DatePicker
                LocalDate selectedDate = eventDatePicker.getValue();
                if (selectedDate != null) {
                    newEvent.setEventDate(selectedDate.toString());
                }
                try {
                    double price = Double.parseDouble(eventPriceField.getText());
                    newEvent.setEventPrice(price);
                } catch (NumberFormatException e) {
                    // Handle invalid price input
                }
                return newEvent;
            }
            return null;
        });

        Optional<Event> result = dialog.showAndWait();
        result.ifPresent(event -> {
            // Add the event to the database
            try {
                if (insertEvent(event)) {
                    // Refresh the table view to display the new event
                    refreshTableView();
                    // Show success message
                    showInfoMessage("Event Created", "The event has been created successfully.");
                } else {
                    // Show error message
                    showErrorMessage("Event Creation Error", "An error occurred while creating the event.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Generate a random event ID within a specified range.
     *
     * @param min The minimum event ID value.
     * @param max The maximum event ID value.
     * @return A randomly generated event ID.
     */
    private int getRandomEventId(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Check if an event ID is unique in the database.
     *
     * @param eventId The event ID to check for uniqueness.
     * @return True if the event ID is unique, false otherwise.
     */
    private boolean isEventIdUnique(int eventId) {
        String sql = "SELECT COUNT(*) FROM event WHERE event_id = ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0; // If count is 0, the eventId is unique
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Handle exceptions or return false if an error occurs
        return false;
    }

    /**
     * Insert a new event into the database.
     *
     * @param event The event to insert into the database.
     * @return True if the event was inserted successfully, false otherwise.
     * @throws SQLException If a database error occurs during insertion.
     */
    private boolean insertEvent(Event event) throws SQLException {
        String sql = "INSERT INTO event (event_id, event_name, event_location, event_date, event_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,event.getEventId());
            preparedStatement.setString(2, event.getEventName());
            preparedStatement.setString(3, event.getEventLocation());
            preparedStatement.setString(4, event.getEventDate());
            preparedStatement.setDouble(5, event.getEventPrice());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Refresh the table view after changes.
     */
    private void refreshTableView() {
        loadDataFromDatabase();
        eventsTableView1.refresh();
        eventsTableView2.refresh();
        eventsTableView3.refresh();
    }

    /**
     * Handle the deletion of an event.
     */
    @FXML
    private void deleteEvent() {
        Event selectedEvent = null;
        ObservableList<Event> selectedEventList = null;

        if (event1Details.isVisible()) {
            selectedEvent = selectedEvent1;
            selectedEventList = eventData1;
        } else if (event2Details.isVisible()) {
            selectedEvent = selectedEvent2;
            selectedEventList = eventData2;
        } else if (event3Details.isVisible()) {
            selectedEvent = selectedEvent3;
            selectedEventList = eventData3;
        }

        if (selectedEvent != null && selectedEventList != null) {
            int eventIdToDelete = selectedEvent.getEventId();

            // Ask for confirmation before deleting
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Delete Event");
            confirmation.setContentText("Are you sure you want to delete this event?");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = deleteEventFromDatabase(eventIdToDelete);

                if (isDeleted) {
                    selectedEventList.remove(selectedEvent);
                    refreshTableView();
                } else {
                    // Handle deletion error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Event Deletion Error");
                    alert.setContentText("An error occurred while deleting the event.");
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Delete an event from the database.
     *
     * @param eventIdToDelete The ID of the event to delete.
     * @return True if the event was deleted successfully, false otherwise.
     */
    private boolean deleteEventFromDatabase(int eventIdToDelete) {
        String sql = "DELETE FROM event WHERE event_id = ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, eventIdToDelete);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Handle event selection for TableView1
    @FXML
    private void handleEventSelection1(MouseEvent event) {
        selectedEvent1 = eventsTableView1.getSelectionModel().getSelectedItem();
    }

    // Handle event selection for TableView2
    @FXML
    private void handleEventSelection2(MouseEvent event) {
        selectedEvent2 = eventsTableView2.getSelectionModel().getSelectedItem();
    }

    // Handle event selection for TableView3
    @FXML
    private void handleEventSelection3(MouseEvent event) {
        selectedEvent3 = eventsTableView3.getSelectionModel().getSelectedItem();
    }

    // Show more details for TableView1
    @FXML
    private void showMoreDetails1(ActionEvent selectedEvent1) {
        loadDataFromDatabase();
        // Show TableView1 and hide others
        mainContentView.setVisible(false);
        event1Details.setVisible(true);
        event2Details.setVisible(false);
        event3Details.setVisible(false);
        event1createEventButton.setVisible(userRole == 2);
        event1deleteEventButton.setVisible(userRole == 2);
        event2createEventButton.setVisible(userRole == 2);
        event2deleteEventButton.setVisible(userRole == 2);
        event3createEventButton.setVisible(userRole == 2);
        event3deleteEventButton.setVisible(userRole == 2);
    }

    // Show more details for TableView1
    @FXML
    private void showMoreDetails2(ActionEvent selectedEvent2) {
        loadDataFromDatabase();
        // Show TableView2 and hide others
        mainContentView.setVisible(false);
        event1Details.setVisible(false);
        event2Details.setVisible(true);
        event3Details.setVisible(false);
    }

    // Show more details for TableView3
    @FXML
    private void showMoreDetails3(ActionEvent selectedEvent3) {
        loadDataFromDatabase();
        // Show TableView3 and hide others
        mainContentView.setVisible(false);
        event1Details.setVisible(false);
        event2Details.setVisible(false);
        event3Details.setVisible(true);
    }

    // Return to the main content view
    @FXML
    private void mainContentView() {
        mainContentView.setVisible(true);
        event1Details.setVisible(false);
        event2Details.setVisible(false);
        event3Details.setVisible(false);
    }

    /**
     * Show an information message dialog.
     *
     * @param title   The title of the information message.
     * @param content The content of the information message.
     */
    private void showInfoMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show an error message dialog.
     *
     * @param title   The title of the error message.
     * @param content The content of the error message.
     */
    private void showErrorMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}