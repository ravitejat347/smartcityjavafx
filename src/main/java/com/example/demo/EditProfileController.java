package com.example.demo;
/**
 * Author: Owen
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller class for editing user profiles.
 */
public class EditProfileController implements Initializable{

    @FXML
    private TextField emailIdTextField;
    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField zipCodeTextField;

    @FXML
    private Button signUp;

    @FXML
    private ChoiceBox stateChoice;

    private int stateId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Fill text fields with user information
        emailIdTextField.setText(User.getInstance().getEmail());
        phoneNumberTextField.setText(User.getInstance().getPhoneNumber());
        firstNameTextField.setText(User.getInstance().getFirstName());
        lastNameTextField.setText(User.getInstance().getLastName());
        addressTextField.setText(User.getInstance().getStreetAddress());
        cityTextField.setText(User.getInstance().getCity());
        zipCodeTextField.setText(User.getInstance().getZipcode());

        final String SELECT_QUERY = "SELECT * FROM states"; // Include stateid in the query
        String[] stateCodes = new String[50];
        Map<String, Integer> stateIdMap = new HashMap<>(); // To store the mapping of state code to state id

        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                int stateId = resultSet.getInt("state_id");
                String stateCode = resultSet.getString("state_code");
                stateCodes[i] = stateCode;
                i++;
                stateIdMap.put(stateCode, stateId);
            }
            System.out.println(stateCodes);

            // Create the ChoiceBox and set the default value to the first option
            ObservableList<String> stateList = FXCollections.observableArrayList(stateCodes);
            stateChoice.setItems(stateList);
            stateChoice.getSelectionModel().selectFirst();

            // Add an event listener to handle user selection
            stateChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    int selectedStateId = stateIdMap.get(newValue); // Get the stateid based on the selected state code
                    System.out.println("Selected State Code: " + newValue);
                    System.out.println("Corresponding State ID: " + selectedStateId);
                    stateId = selectedStateId;
                }
            });

        } catch (SQLException e) {
            // Handle the SQL exception
            printSQLException(e);
        }



    }

    /**
     * Navigates the user back to the landing page.
     */
    public void backToLanding() {
        try {
            // Load the LandingPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing-page.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) signUp.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the editing of the user profile.
     *
     * @param event The ActionEvent triggered by the "Edit Profile" button.
     */
    public void editProfile( ActionEvent event ) {

        Window owner = signUp.getScene().getWindow();

        // Email validation
        if (emailIdTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email");
            return;
        }
        if (phoneNumberTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your phone number");
            return;
        }
        // Password validation
        if (firstNameTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your first name");
            return;
        }
        if (lastNameTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your last name");
            return;
        }
        if (addressTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Street Address");
            return;
        }
        if (cityTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the City");
            return;
        }
        if (zipCodeTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Zip Code");
            return;
        }
        if (zipCodeTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your Zip Code");
            return;
        }

        // Save user input from text fields
        String email = emailIdTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String streetAddress = addressTextField.getText();
        String city = cityTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String zipCode = zipCodeTextField.getText();

        // Check if an account with the entered email already exists
        boolean flag = checkForEmail(email);
        System.out.println("Flag: "+flag);

        if (flag == true) {
            // Don't edit profile
            infoBox("An account with this email already exists.", null, "Failed");
            return;
        } else {
            // Edit profile
            updateProfile(email, firstName, lastName, streetAddress, city, phoneNumber, zipCode);
            // Update User instance
            User.getInstance().setEmail(email);
            User.getInstance().setFirstName(firstName);
            User.getInstance().setLastName(lastName);
            User.getInstance().setStreetAddress(streetAddress);
            User.getInstance().setCity(city);
            User.getInstance().setPhoneNumber(phoneNumber);
            User.getInstance().setZipcode(zipCode);
            infoBox("Edit profile Successful", null, "Success");

            // Send User back to Landing
            backToLanding();
        }

    } // End of signUp

    /**
     * Checks to see if the entered email matches the currect user
     * Then checks if the email is tied to an account if it doesn't match
     * @param emailId
     * @return
     */
    public static boolean checkForEmail(String emailId) {

        // Check if the entered email matches the current account
        if (!emailId.equals(User.getInstance().getEmail())) {
            // Then check if the email is already tied to an account
            final String SELECT_QUERY = "SELECT * FROM user WHERE user_email = ?";
            try (Connection connection = DBConn.connectDB();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
                preparedStatement.setString(1, emailId);

                System.out.println(preparedStatement);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next() == true) {
                    System.out.println("An account with this email already exists.");
                    return true;
                }


            } catch (SQLException e) {
                // print SQL exception information
                printSQLException(e);
            }
        }
        return false;
    } // End of checkForEmail

    /**
     * Updates the user's profile information in the database.
     *
     * @param emailId     The user's email.
     * @param fName       The user's first name.
     * @param lName       The user's last name.
     * @param streetAdd   The user's street address.
     * @param city        The user's city.
     * @param phoneNumber The user's phone number.
     * @param zipCode     The user's zip code.
     */
    public static void updateProfile(String emailId, String fName, String lName, String streetAdd, String city, String phoneNumber, String zipCode) {

        int role = User.getInstance().getRoleID();
        // Everything is ok, start updating the database
        final String UPDATE_QUERY = "UPDATE user" +
                " SET first_name = ?" +
                ", last_name = ?" +
                ", user_streetaddress = ?" +
                ", user_city = ?" +
                ", user_zipcode = ?" +
                ", user_email = ?" +
                ", user_phone = ?" +
                ", role_ID = ?" +
                " WHERE user_email = ?";
        // try connecting to the database
        try (Connection connection = DBConn.connectDB()) {

            connection.setAutoCommit(false); // Disable auto-commit
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, streetAdd);
            preparedStatement.setString(4, city);
            preparedStatement.setString(5, zipCode);
            preparedStatement.setString(6, emailId);
            preparedStatement.setString(7, phoneNumber);
            preparedStatement.setInt(8, role);
            preparedStatement.setString(9, emailId);

            // execute query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit(); // Commit the transaction
            } else {
                connection.rollback(); // Rollback if the update fails
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    /**
     * Displays an informational dialog box.
     *
     * @param infoMessage The message to be displayed.
     * @param headerText  The header text for the dialog box.
     * @param title       The title of the dialog box.
     */
    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    } // End of infoBox

    /**
     * Shows an alert dialog box with the specified alert type, title, and message.
     *
     * @param alertType The type of alert (e.g., ERROR, INFORMATION, etc.).
     * @param owner     The owner window of the alert.
     * @param title     The title of the alert.
     * @param message   The message to be displayed in the alert.
     */
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    } // End of showAlert

    /**
     * Prints SQL exception details to the standard error stream.
     *
     * @param ex The SQL exception to be printed.
     */
    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    } // End of printSQLException
}
