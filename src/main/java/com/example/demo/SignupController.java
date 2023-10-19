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
import java.util.*;

/**
 * Controller class for the signup screen.
 */
public class SignupController implements Initializable {

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
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private ChoiceBox stateChoice;

    private int stateId;

    /**
     * Initializes the signup form, including loading the list of states into the ChoiceBox.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources for the root object, or null if this object was not created from a FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final String SELECT_QUERY = "SELECT * FROM states"; // Include stateid in the query
        String[] stateCodes = new String[50];
        Map<String, Integer> stateIdMap = new HashMap<>(); // To store the mapping of state code to state id

        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

                ResultSet resultSet = preparedStatement.executeQuery();
                int i = 0;
                while (resultSet.next()) {
                    int stateId = resultSet.getInt("state_id");
                    String stateCode = resultSet.getString("state_code");
                    stateCodes[i] = stateCode;
                    i++;
                    stateIdMap.put(stateCode, stateId);
                }
                System.out.println(Arrays.toString(stateCodes));

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

            }
        } catch (SQLException e) {
            // Handle the SQL exception
            printSQLException(e);
        }
    }

    /**
     * Handles the "Back to Login" button click event.
     *
     * @param event The ActionEvent object representing the event.
     * @throws IOException  If an I/O error occurs during FXML loading.
     */
    public void BackToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("logged-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 544, 400);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("Smart City - Sign up");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the "Sign Up" button click event.
     *
     */
    public void signUp() {

        Window owner = signUp.getScene().getWindow();

        // Email validation
        if (emailIdTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your email");
            return;
        }
        if (phoneNumberTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your phone number");
            return;
        }
        // Password validation
        if (firstNameTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your first name");
            return;
        }
        if (lastNameTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your last name");
            return;
        }
        if (addressTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your Street Address");
            return;
        }
        if (cityTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter the City");
            return;
        }
        if (zipCodeTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your Zip Code");
            return;
        }
        if (zipCodeTextField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your Zip Code");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(owner,
                    "Please enter your Password");
            return;
        }
        if (confirmPassword.getText().isEmpty()) {
            showAlert(owner,
                    "Please confirm your password");
            return;
        }
        if(!passwordField.getText().equals(confirmPassword.getText())){
            showAlert(owner,
                    "Password do not match");
            return;
        }

        // Save user input from text fields
        String email = emailIdTextField.getText();
        String password = passwordField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String streetAddress = addressTextField.getText();
        String city = cityTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String zipCode = zipCodeTextField.getText();

        // Check if an account with the entered email already exists
        boolean flag = checkForEmail(email);
        System.out.println("Flag: "+flag);

        if (flag) {
            // Don't create account
            infoBox("An account with this email already exists.", null, "Failed");
        } else {
            // Create account
            addUser(email, password, firstName, lastName, streetAddress, city, stateId, phoneNumber, zipCode, 1);
            infoBox("Registration Successful", null, "Success");

        }

    }

    /**
     * Checks if an email address already exists in the database.
     *
     * @param emailId The email address to check.
     * @return True if the email already exists, false otherwise.
     */
    public static boolean checkForEmail(String emailId) {

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        final String SELECT_QUERY = "SELECT * FROM user WHERE user_email = ?";
        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (// Step 2:Create a statement using connection object
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
                preparedStatement.setString(1, emailId);

                System.out.println(preparedStatement);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("An account with this email already exists.");
                    return true;
                }


            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    } // End of checkForEmail

    /**
     * Adds a new user to the database.
     *
     * @param emailId      The user's email address.
     * @param userPassword The user's password.
     * @param fName        The user's first name.
     * @param lName        The user's last name.
     * @param streetAdd    The user's street address.
     * @param city         The user's city.
     * @param state        The user's state.
     * @param phoneNumber  The user's phone number.
     * @param zipCode      The user's ZIP code.
     * @param roleID       The user's role ID.
     */
    public static void addUser(String emailId, String userPassword, String fName, String lName, String streetAdd, String city, int state, String phoneNumber, String zipCode, int roleID) {

        final String INSERT_QUERY = "INSERT INTO user (first_name, last_name, user_streetaddress, user_city, user_zipcode, state_ID, user_email, user_password, user_phone, role_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {

                preparedStatement.setString(1, fName);
                preparedStatement.setString(2, lName);
                preparedStatement.setString(3, streetAdd);
                preparedStatement.setString(4, city);
                preparedStatement.setString(5, zipCode);
                preparedStatement.setInt(6, state);
                preparedStatement.setString(7, emailId);
                preparedStatement.setString(8, userPassword);
                preparedStatement.setString(9, phoneNumber);
                preparedStatement.setInt(10, roleID);

                preparedStatement.executeUpdate();

                // Check if any rows were inserted (1 row should be inserted for success)

            }
        } catch (SQLException e) {
            // Handle the SQL exception
            printSQLException(e);
        }
    }

    /**
     * Displays an information dialog box.
     *
     * @param infoMessage The message to display.
     * @param headerText  The header text of the dialog box.
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
     * Displays an alert dialog box.
     * @param owner     The owner window of the dialog.
     * @param message   The message to display.
     */
    private static void showAlert(Window owner, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    } // End of showAlert


    /**
     * Prints the details of a SQL exception.
     *
     * @param ex The SQL exception to print.
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
    }
}
