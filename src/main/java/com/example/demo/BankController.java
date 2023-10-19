package com.example.demo;
/**
 * Author: Ravi
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Controller class for managing banks.
 */
public class BankController {

    @FXML
    private VBox bankView;

    @FXML
    private TextField newBankNameField;
    @FXML
    private ListView<String> bankListView;

    @FXML
    private TextField updateBankNameField;

    @FXML
    private VBox deleteAndUpdateContainer;
    // Add a flag to track whether to show the "Delete" and "Update" sections
    private boolean showDeleteAndUpdate = false;
    // ObservableList to store bank names
    public ObservableList<String> bankNames = FXCollections.observableArrayList();

    // Initialize the controller
    public void initialize() {
        bankListView.setCellFactory(TextFieldListCell.forListView());

        // Load bank list when initializing
        loadBankList();
    }

    // Event handler for when a bank is selected in the list
    @FXML
    public void bankSelected() {
        String selectedBank = bankListView.getSelectionModel().getSelectedItem();
        if (selectedBank != null) {
            // Show the Delete and Update sections
            showDeleteAndUpdateSections(true);
        } else {
            // Hide the Delete and Update sections if no bank is selected
            showDeleteAndUpdateSections(false);
        }
    }

    // Event handler for creating a new bank
    @FXML
    public void createNewBank() {
        String newBankName = newBankNameField.getText();
        if (!newBankName.isEmpty()) {
            try (Connection connection = DBConn.connectDB()) {
                String sql = "INSERT INTO bank (bank_name) VALUES (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newBankName);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccessMessage("Bank Created Successfully!");
                    newBankNameField.clear();
                    // Reload and display the updated list of banks
                    loadBankList();
                    // Show the "Delete" and "Update" sections
                   // showDeleteAndUpdateSections(true);
                } else {
                    showErrorMessage("Failed to Create Bank!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorMessage("An error occurred while creating the bank.");
            }
        } else {
            showErrorMessage("Bank name cannot be empty.");
        }
    }

    // Event handler for deleting a selected bank
    @FXML
    public void deleteSelectedBank() {
        String selectedBank = bankListView.getSelectionModel().getSelectedItem();
        if (selectedBank != null) {
            try (Connection connection = DBConn.connectDB()) {
                String sql = "DELETE FROM bank WHERE bank_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, selectedBank);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccessMessage("Bank Deleted Successfully!");
                    // Reload and display the updated list of banks
                    loadBankList();
                } else {
                    showErrorMessage("Failed to Delete Bank!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorMessage("An error occurred while deleting the bank.");
            }
        } else {
            showErrorMessage("Please select a bank to delete.");
        }
    }

    // Helper method to toggle the visibility of "Delete" and "Update" sections
    private void showDeleteAndUpdateSections(boolean show) {
        deleteAndUpdateContainer.setVisible(show);
    }

    // Load the list of banks from the database
    private void loadBankList() {
        bankNames.clear();

        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT bank_name FROM bank";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bankNames.add(resultSet.getString("bank_name"));
            }

            bankListView.setItems(bankNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Event handler for updating the selected bank
    @FXML
    public void updateSelectedBank() {
        String selectedBank = bankListView.getSelectionModel().getSelectedItem();
        String updatedBankName = updateBankNameField.getText();

        if (selectedBank != null && !updatedBankName.isEmpty()) {
            try (Connection connection = DBConn.connectDB()) {
                String sql = "UPDATE bank SET bank_name = ? WHERE bank_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, updatedBankName);
                preparedStatement.setString(2, selectedBank);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccessMessage("Bank Updated Successfully!");
                    updateBankNameField.clear();
                    // Reload and display the updated list of banks
                    loadBankList();
                } else {
                    showErrorMessage("Failed to Update Bank!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorMessage("An error occurred while updating the bank.");
            }
        } else {
            showErrorMessage("Please select a bank and enter an updated bank name.");
        }
    }

    // Event handler for navigating back to the landing page
    @FXML
    public void navigateBackToLandingPage() {
        try {
            // Load the LandingPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing-page.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) bankView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Event handler for showing bank accounts
    @FXML
    public void showBankAccounts() {
        try {
            // Load the Bank.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bankAccount.fxml"));
            Parent root = loader.load();
            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) bankView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to show a success message dialog
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to show an error message dialog
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
