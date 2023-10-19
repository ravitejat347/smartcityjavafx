package com.example.demo;

/**
 * Author: Kevin
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 * A list of useful method
 * often a bit too late
 */
public class SQLHelper {
    static Connection connection = DBConn.connectDB();

    public static ResultSet makeQuery(String sql) {
        ResultSet rs = null;
        try {
            PreparedStatement selectStatement = connection.prepareStatement(sql);
            rs = selectStatement.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.print(e.getStackTrace());
        }
        return rs;
    }

    public static void deleteQuery(String sql){
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeQuery();
            System.out.print(sql+ " Executed");
        } catch (SQLException e) {
            System.out.print(e.getStackTrace());
        }
    }
}
