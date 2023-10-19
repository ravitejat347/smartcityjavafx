package com.example.demo;
/**
 * Author: Maaz, Owen
 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller class for the logged-in view.
 */
public class LoggedinController {

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    static Connection connection = DBConn.connectDB();

    /**
     * Handles the login action when the submit button is clicked.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException  If an I/O error occurs.
     */
    @FXML
    public void login(ActionEvent event) throws IOException {
        Window owner = submitButton.getScene().getWindow();

        if (emailIdField.getText().isEmpty()) {
            showAlert(owner, "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(owner, "Please enter a password");
            return;
        }

        String emailId = emailIdField.getText();
        String password = passwordField.getText();

        boolean flag = validate(emailId, password);

        if (flag) {
            infoBox("Login Successful!", null, "Success");
            // Load the main application window
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("landing-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 681);
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setTitle("Smart City");
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } else {
            infoBox("Please enter correct Email and Password", null, "Failed");
        }
    }

    /**
     * Handles the sign-up action when the sign-up button is clicked.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException  If an I/O error occurs.
     */
    @FXML
    public void goToSignUp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sign-up.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 535, 400);
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("Smart City - Sign up");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Displays an information dialog box with the given message.
     *
     * @param infoMessage The message to display.
     * @param headerText  The header text for the dialog box.
     * @param title       The title of the dialog box.
     */
    public static void infoBox(String infoMessage, String headerText, String title) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText(infoMessage);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.showAndWait();
        });
    }

    /**
     * Displays an alert dialog with the specified alert type, title, and message.
     * @param owner     The owner window of the alert.
     * @param message   The message to display in the alert.
     */
    private static void showAlert(Window owner, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Form Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    /**
     * Validates the user's login credentials.
     *
     * @param emailId  The user's email ID.
     * @param password The user's password.
     * @return True if the credentials are valid, false otherwise.
     */
    public static boolean validate(String emailId, String password){
        final String SELECT_QUERY = "SELECT * FROM user WHERE user_email = ? and user_password = ?";

        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
                preparedStatement.setString(1, emailId);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM states WHERE state_ID = ?");
                    preparedStatement2.setInt(1, Integer.parseInt(resultSet.getString(7)));
                    ResultSet stateResultSet = preparedStatement2.executeQuery();

                    if (stateResultSet.next()) {
                        User.initializeUser(Integer.parseInt(resultSet.getString(1)),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6),
                                stateResultSet.getString(2),
                                resultSet.getString(8),
                                resultSet.getString(10),
                                Integer.parseInt(resultSet.getString(11)));
                        System.out.println("Logged in!");
                        HotelBooking.getInstance().setEmailId(emailId);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    /**
     * Prints SQL exception details.
     *
     * @param ex The SQLException.
     */
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
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

