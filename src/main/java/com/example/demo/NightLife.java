package com.example.demo;
/**
 * Author: Rahul
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents information about NightLife.
 */
public class NightLife {
    private final String name;
    private final String description;
    private final String imageUrl;
    private final String moreInfoUrl;

    /**
     * Constructor to create a NightLife object.
     *
     * @param name         The name of the NightLife.
     * @param description  The description of the NightLife.
     * @param imageUrl     The URL of the NightLife's image.
     * @param moreInfoUrl  The URL for more information about the NightLife.
     */
    public NightLife(String name, String description, String imageUrl, String moreInfoUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.moreInfoUrl = moreInfoUrl;
    }

    /**
     * Get the name of the NightLife.
     *
     * @return The name of the NightLife.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the NightLife.
     *
     * @return The description of the NightLife.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the URL of the NightLife's image.
     *
     * @return The URL of the NightLife's image.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Get the URL for more information about the NightLife.
     *
     * @return The URL for more information about the NightLife.
     */
    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }

    /**
     * Retrieve a list of NightLife information from the database.
     *
     * @return A list of NightLife objects.
     */
    public static List<NightLife> getNightlifeInfo() {
        List<NightLife> nightlifeList = new ArrayList<>();
        String sql = "SELECT * FROM nightlife";
        try (Connection connection = DBConn.connectDB()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    NightLife nightlife = new NightLife(
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("imageUrl"),
                            resultSet.getString("moreInfoUrl")
                    );
                    nightlifeList.add(nightlife);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nightlifeList;
    }
}
