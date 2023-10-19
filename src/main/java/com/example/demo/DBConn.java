package com.example.demo;
/**
 * Author: Maaz
 */

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Provides a utility class for establishing a database connection.
 */
public class DBConn {
    /**
     * Establishes a database connection using the MySQL JDBC driver.
     *
     * @return A Connection object representing the database connection.
     */
    public static Connection connectDB(){
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection with the specified URL, username, and password.
            connection = DriverManager.getConnection(
                    "jdbc:mysql://smartcity-db2.ctpu1etrkqud.us-east-1.rds.amazonaws.com:3306/smartcity",
                    "admin", "maaz2023");
            return connection;
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }
}
