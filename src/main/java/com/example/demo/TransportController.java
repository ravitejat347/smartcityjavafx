package com.example.demo;
/**
 * Auhtor: Maaz
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class manages transportation-related data and operations.
 */
public class TransportController {

    // List to store bus objects
    static ArrayList<Bus> buses = new ArrayList<>();
    static Connection connection = DBConn.connectDB();

    /**
     * Retrieves a list of buses from the database.
     * @return An ArrayList of Bus objects.
     * @throws SQLException if a database error occurs.
     */
    public static ArrayList<Bus> getBuses() throws SQLException {
        String busQ = "SELECT route_id, route_name, route_busNumber, route_desc FROM transport_route";
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(busQ)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    String routeName = resultSet.getString("route_name");
                    String busNum = resultSet.getString("route_busNumber");
                    String busDesc = resultSet.getString("route_desc");
                    String routeMainId = resultSet.getString("route_id");
                    buses.add(new Bus(busNum,routeName,busDesc, routeMainId));
                }
                return buses;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of stops for a given route.
     * @param routeId The ID of the route for which stops are retrieved.
     * @return An ArrayList of Stop objects.
     * @throws SQLException if a database error occurs.
     */
    public static ArrayList<Stop> getStops(String routeId) throws SQLException {
        ArrayList<Stop> stops = new ArrayList<>();

        // Use SQL joins with distinct aliases for tables
        String query = "SELECT ts.stop_name, tt.arrival_time, tt.departure_time " +
                "FROM transport_trip3 ttrip " +
                "JOIN transport_time3 tt ON ttrip.trip_id = tt.trip_id " +
                "JOIN transport_stop ts ON tt.stop_id = ts.stop_id " +
                "WHERE ttrip.route_id = ?";

        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, routeId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    SimpleStringProperty stopName = new SimpleStringProperty(resultSet.getString("stop_name"));
                    SimpleStringProperty arrivalTime = new SimpleStringProperty(resultSet.getString("arrival_time"));
                    SimpleStringProperty departureTime = new SimpleStringProperty(resultSet.getString("departure_time"));
                    stops.add(new Stop(stopName, arrivalTime, departureTime));
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return stops;
    }

    /**
     * Retrieves the account balance for a given account number.
     * @param accountNumber The account number.
     * @return The account balance.
     */
    public static double getAccountBalance(int accountNumber) {

        try {
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

    /**
     * Retrieves the account ID for a given user ID.
     * @param userId The user ID.
     * @return The account ID.
     */
    public static int getAccountId(int userId) {
        int accountId = -1; // Default value in case of an error

        try (Connection connection = DBConn.connectDB()) {
            String sql = "SELECT account_no FROM bank_account WHERE user_id = ?";
            assert connection != null;
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

    /**
     * Updates the account balance for a given account number.
     * @param accountNumber The account number.
     * @param amount The new account balance.
     */
    public static void updateAccountBalance(int accountNumber, double amount) {
        try{
            connection.setAutoCommit(false); // Disable auto-commit
            String sql = "UPDATE bank_account SET balance = ? WHERE account_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, accountNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit(); // Commit the transaction
            } else {
                connection.rollback(); // Rollback if the update fails
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Purchases bus tickets for the user.
     * @param qty The quantity of tickets to purchase.
     */
    public static void purchaseTicket(int qty){
        if((1.50 * qty) >= getAccountBalance(getAccountId(User.getInstance().getUserID()))){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application Status");
            alert.setHeaderText(null);
            alert.setContentText("Sorry, Not Enough Funds!");
            alert.showAndWait();
        }
        else{
            double bankAmount = getAccountBalance(getAccountId(User.getInstance().getUserID())) - (1.50 * qty);
            updateAccountBalance(getAccountId(User.getInstance().getUserID()), bankAmount);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application Status");
            alert.setHeaderText(null);
            alert.setContentText("Congratulations, you bought " + qty + " bus tickets for $" + (1.50 * qty));
            alert.showAndWait();
        }
    }
}
