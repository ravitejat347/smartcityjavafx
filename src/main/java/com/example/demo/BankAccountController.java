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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Controller class for managing bank accounts and transactions.
 */
public class BankAccountController {

    @FXML
    private VBox bankAccountView;
    @FXML
    private TextField emailIdField;
    @FXML
    private Label bankNameLabel;

    @FXML
    private ListView<String> bankListView;

    @FXML
    private ComboBox<String> bankSelectionComboBox;

    private static int userId;

    public ObservableList<String> bankNames = FXCollections.observableArrayList();
    // Create an ObservableList to hold bank account data
    private ObservableList<String> bankAccountData = FXCollections.observableArrayList();
    @FXML
    private Button backToBankButton;
    @FXML
    private Button backToLandingPageButton;
    private int userRole = User.getInstance().getRoleID();
    @FXML
    public void initialize() {
        // Bind the ListView to the ObservableList
        bankListView.setItems(bankAccountData);
        // Call loadBankNames when the controller is initialized
        loadBankNames();
        loadBankAccounts();

    }

    // Initialize the controller with data for the selected bank.
    public void initData(String selectedBank) {
        bankNameLabel.setText("Bank Name: " + selectedBank);
        loadBankAccounts();
    }

    // Load available bank names into the ComboBox.
    private void loadBankNames() {
        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT bank_name FROM bank";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bankNames.add(resultSet.getString("bank_name"));
            }

            bankSelectionComboBox.setItems(bankNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get the user ID based on their email.
    public int getUserId(){
        String userEmail = HotelBooking.getInstance().getEmailId();
        HotelBooking c = new HotelBooking();
        userId = c.getUserdetails(userEmail);
        return userId;
    }

    // Create a new bank account for the selected user.
    @FXML
    public void createNewBankAccount() {
        // Get the input values
        int accountNumber = generateRandomAccountNumber();
        int routingNumber = generateRandomRoutingNumber();
        double balance = 0;

        // Retrieve the selected bank from the ComboBox
        String selectedBank = bankSelectionComboBox.getValue();

        if (selectedBank != null && !selectedBank.isEmpty()) {
            try (Connection connection = DBConn.connectDB()) {
                // Get the bank_id based on the selected bank name
                int bankId = getBankId(selectedBank);

                userId = getUserId();

                if (userId > 0) {
                    // Check if the account number is unique within the selected bank
                    if (isAccountNumberUnique(accountNumber, bankId)) {
                        // Insert a new bank account
                        String sql = "INSERT INTO bank_account (account_no, user_id, bank_id, routing_no, balance) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, accountNumber);
                        preparedStatement.setInt(2, userId);
                        preparedStatement.setInt(3, bankId);
                        preparedStatement.setInt(4, routingNumber);
                        preparedStatement.setDouble(5, balance);

                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            showSuccessMessage("Bank Account Created Successfully!");
                            // After successfully creating a bank account, add it to the ListView
                            String accountText = "Account No: " + accountNumber + " - User ID: " + userId;
                            bankListView.getItems().add(accountText);
                            loadBankAccounts();
                        } else {
                            showErrorMessage("Failed to Create Bank Account.");
                        }
                    } else {
                        showErrorMessage("Account number must be unique within the selected bank.");
                    }
                } else {
                    showErrorMessage("User with the specified email not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorMessage("An error occurred while creating the bank account.");
            }
        } else {
            showErrorMessage("Please select a bank.");
        }
    }

    // Get the bank ID based on the selected bank name.
    private int getBankId(String selectedBank) {
        int bankId = -1; // Default value in case of an error

        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT bank_id FROM bank WHERE bank_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, selectedBank);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bankId = resultSet.getInt("bank_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bankId;
    }

    // Delete the selected bank account.
    @FXML
    public void deleteSelectedBank() {
        String selectedBankAccountText = bankListView.getSelectionModel().getSelectedItem();
        if (selectedBankAccountText != null) {
            int accountNumber = extractAccountNumberFromText(selectedBankAccountText);

            if (accountNumber != -1) {

                try (Connection connection = DBConn.connectDB()) {
                    // Implement the logic to delete the bank account based on account number
                    String sql = "DELETE FROM bank_account WHERE account_no = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, accountNumber);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        showSuccessMessage("Bank Account Deleted Successfully!");
                        // Remove the deleted account from the ListView
                        bankListView.getItems().remove(selectedBankAccountText);
                    } else {
                        showErrorMessage("Failed to Delete Bank Account.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showErrorMessage("An error occurred while deleting the bank account.");
                }
            } else {
                showErrorMessage("Invalid account number extracted.");
            }
        } else {
            showErrorMessage("Please select a bank account to delete.");
        }
    }

    // Extract the account number from the selected bank account text.
    private int extractAccountNumberFromText(String text) {

        // Define a regular expression pattern to match the "Account No:" part and extract the number
        Pattern pattern = Pattern.compile("Account No:\\s*(-?\\d+)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String accountNumberStr = matcher.group(1).trim();
            if (!accountNumberStr.isEmpty()) {
                try {
                    return Integer.parseInt(accountNumberStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // Handle cases where the "Account No:" information is missing or empty
        System.err.println("No valid account number found.");
        return -1;
    }

    // Update the selected bank account
    @FXML
    public void updateSelectedBank() {
        String selectedBankAccountText = bankListView.getSelectionModel().getSelectedItem();
        if (selectedBankAccountText != null) {
            int accountNumber = extractAccountNumberFromText(selectedBankAccountText);
            // Implement the logic to update a bank account based on the account number
            // Replace this with database update logic
        } else {
            showErrorMessage("Please select a bank account to update.");
        }
    }

    // Deposit money into the selected bank account.
    @FXML
    public void depositMoney() {
        String selectedBankAccountText = bankListView.getSelectionModel().getSelectedItem();

        if (selectedBankAccountText != null) {
            int accountNumber = extractAccountNumberFromText(selectedBankAccountText);

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Deposit Money");
            dialog.setHeaderText("Enter the deposit amount:");
            dialog.setContentText("Amount:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                try {
                    double depositAmount = Double.parseDouble(result.get());

                    if (depositAmount > 0) {
                        // Implement the logic to update the balance in the database.
                        boolean depositSuccess = updateAccountBalance(accountNumber, depositAmount);

                        if (depositSuccess) {
                            // After successful deposit, fetch updated details and update the ListView
                            loadBankAccounts();
                            showSuccessMessage("Deposit Successful!");
                        } else {
                            showErrorMessage("Failed to update the balance.");
                        }
                    } else {
                        showErrorMessage("Please enter a positive deposit amount.");
                    }
                } catch (NumberFormatException e) {
                    showErrorMessage("Invalid deposit amount format.");
                }
            }
        } else {
            showErrorMessage("Please select a bank account to deposit money.");
        }
    }

    // Update the balance of a bank account.
    private boolean updateAccountBalance(int accountNumber, double depositAmount) {
        try (Connection connection = DBConn.connectDB()) {
            connection.setAutoCommit(false); // Disable auto-commit

            String sql = "UPDATE bank_account SET balance = balance + ? WHERE account_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, depositAmount);
            preparedStatement.setInt(2, accountNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit(); // Commit the transaction
                return true;
            } else {
                connection.rollback(); // Rollback if the update fails
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Withdraw money from the selected bank account.
    @FXML
    public void withdrawMoney() {
        String selectedBankAccountText = bankListView.getSelectionModel().getSelectedItem();

        if (selectedBankAccountText != null) {
            int accountNumber = extractAccountNumberFromText(selectedBankAccountText);

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Withdraw Money");
            dialog.setHeaderText("Enter the withdrawal amount:");
            dialog.setContentText("Amount:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                try {
                    double withdrawalAmount = Double.parseDouble(result.get());

                    if (withdrawalAmount > 0) {
                        // Check if withdrawal amount is greater than the account balance
                        double currentBalance = getAccountBalance(accountNumber);
                        if (withdrawalAmount > currentBalance) {
                            showErrorMessage("Insufficient funds.");
                        } else {
                            // Implement the logic to update the balance in the database.
                            boolean withdrawalSuccess = updateAccountBalance(accountNumber, -withdrawalAmount);

                            if (withdrawalSuccess) {
                                // After successful withdrawal, fetch updated details and update the ListView
                                loadBankAccounts();
                                showSuccessMessage("Withdrawal Successful!");
                            } else {
                                showErrorMessage("Failed to update the balance.");
                            }
                        }
                    } else {
                        showErrorMessage("Please enter a positive withdrawal amount.");
                    }
                } catch (NumberFormatException e) {
                    showErrorMessage("Invalid withdrawal amount format.");
                }
            }
        } else {
            showErrorMessage("Please select a bank account to withdraw money from.");
        }
    }

    // Show the balance of the selected bank account.
    @FXML
    public void showBalance() {
        String selectedBankAccountText = bankListView.getSelectionModel().getSelectedItem();
        if (selectedBankAccountText != null) {
            int accountNumber = extractAccountNumberFromText(selectedBankAccountText);
            double balance = getAccountBalance(accountNumber);
            showSuccessMessage("Account Balance: $" + balance);
        } else {
            showErrorMessage("Please select a bank account to show balance.");
        }
    }

    // Navigate back to the bank view.
    @FXML
    public void navigateBackToBank() {
        try {
            // Load the LandingPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bank.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) bankAccountView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Navigate back to the landing page.
    @FXML
    public void navigateBackToLandingPage() {
        try {
            // Load the LandingPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing-page.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) bankAccountView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load bank accounts based on the user's role.
    private void loadBankAccounts() {
        bankListView.getItems().clear(); // Clear the ListView before loading new accounts
        if(userRole == 1){
            try (Connection connection = DBConn.connectDB()) {
                String sql = "SELECT ba.account_no, ba.user_id, ba.bank_id, ba.routing_no, ba.balance, b.bank_name " +
                        "FROM bank_account ba " +
                        "JOIN bank b ON ba.bank_id = b.bank_id " +
                        "WHERE ba.user_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, getUserId());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int accountNumber = resultSet.getInt("account_no");
                    int userId = resultSet.getInt("user_id");
                    String bankName = resultSet.getString("bank_name");
                    int routingNumber = resultSet.getInt("routing_no");
                    double balance = resultSet.getDouble("balance");
                    BankAccountController c = new BankAccountController();

                    String name = c.getName(userId);

                    // Create a formatted account text with bank name
                    String accountText = "Bank: " + bankName + " - Account No: "
                            + accountNumber + " - Name: " + name + " - Routing No: " + routingNumber + " - Balance:$" + balance;
                    bankListView.getItems().add(accountText); // Add the account to the ListView
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (userRole == 2){
            try (Connection connection = DBConn.connectDB()) {
                String sql = "SELECT ba.account_no, ba.user_id, ba.bank_id, ba.routing_no, ba.balance, b.bank_name " +
                        "FROM bank_account ba " +
                        "JOIN bank b ON ba.bank_id = b.bank_id ";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
              //  preparedStatement.setInt(1, getUserId());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int accountNumber = resultSet.getInt("account_no");
                    int userId = resultSet.getInt("user_id");
                    String bankName = resultSet.getString("bank_name");
                    int routingNumber = resultSet.getInt("routing_no");
                    double balance = resultSet.getDouble("balance");
                    BankAccountController c = new BankAccountController();

                    String name = c.getName(userId);
                    // Create a formatted account text with bank name
                    String accountText = "Bank: " + bankName + " - Account No: "
                            + accountNumber + " - Name: " + name + " - Routing No: " + routingNumber + " - Balance:$" + balance;
                    bankListView.getItems().add(accountText); // Add the account to the ListView
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName(int userId){
        String sql = "SELECT first_name, last_name FROM user where uid= ?";
        try (Connection connection = DBConn.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                return firstName + " " + lastName;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    // Get the balance of a bank account.
    private double getAccountBalance(int accountNumber) {
        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT balance FROM bank_account WHERE account_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return a default value if there was an error or if the account doesn't exist
        return 0.0;
    }

    // Get the balance of a bank account.
    private boolean isAccountNumberUnique(int accountNumber, int bankId) {
        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT COUNT(*) AS count FROM bank_account WHERE account_no = ? AND bank_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setInt(2, bankId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                // If count is 0, it means the account number is unique for the selected bank
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // In case of an error, return false to be on the safe side
        return false;
    }

    // Generate a random account number of 8 digits
    private int generateRandomAccountNumber() {
        Random random = new Random();
        int accountNumber = 0;

        // Generate a random 8-digit account number
        for (int i = 0; i < 8; i++) {
            accountNumber = accountNumber * 10 + random.nextInt(10);
        }

        return accountNumber;
    }

    // Generate a random routing number of 8 digits
    private int generateRandomRoutingNumber() {
        Random random = new Random();
        int routingNumber = 0;

        // Generate a random 8-digit routing number
        for (int i = 0; i < 8; i++) {
            routingNumber = routingNumber * 10 + random.nextInt(10);
        }

        return routingNumber;
    }

    // Show a success message dialog.
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show an error message dialog.
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
