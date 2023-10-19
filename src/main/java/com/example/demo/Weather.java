package com.example.demo;
/**
 * Author: Maaz
 */

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A class for retrieving weather information from a weather API.
 */
public class Weather {

    // Weather data fields
    private final double tempFarhenhiet;
    private final double windMph;
    private final double precipIn;
    private final int humidity;
    private final int uvIndex;
    private final String conditionIcon;
    private final String conditionText;

    /**
     * Constructor to initialize weather data.
     * @param tempFarhenhiet Temperature in Fahrenheit.
     * @param windMph Wind speed in miles per hour.
     * @param humidity Humidity percentage.
     * @param uvIndex UV index.
     * @param precipIn Precipitation in inches.
     * @param conditionIcon Icon representing the weather condition.
     * @param conditionText Text description of the weather condition.
     */
    public Weather(double tempFarhenhiet, double windMph, int humidity,
                   int uvIndex, double precipIn, String conditionIcon, String conditionText){
        this.tempFarhenhiet = tempFarhenhiet;
        this.windMph = windMph;
        this.precipIn = precipIn;
        this.humidity = humidity;
        this.uvIndex = uvIndex;
        this.conditionIcon = conditionIcon;
        this.conditionText =conditionText;
    }

    /**
     * Get the temperature in Fahrenheit.
     * @return Temperature in Fahrenheit.
     */
    public double getTempFarhenhiet() {
        return tempFarhenhiet;
    }

    /**
     * Get the wind speed in miles per hour.
     * @return Wind speed in miles per hour.
     */
    public double getWindMph() {
        return windMph;
    }

    /**
     * Get the precipitation in inches.
     * @return Precipitation in inches.
     */
    public double getPrecipIn() {
        return precipIn;
    }

    /**
     * Get the humidity percentage.
     * @return Humidity percentage.
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * Get the UV index.
     * @return UV index.
     */
    public int getUvIndex() {
        return uvIndex;
    }

    /**
     * Get the weather condition icon.
     * @return Weather condition icon.
     */
    public String getConditionIcon() {
        return conditionIcon;
    }

    /**
     * Get the weather condition text description.
     * @return Weather condition text description.
     */
    public String getConditionText() {
        return conditionText;
    }

    /**
     * Retrieve weather data from a weather API.
     * @return Weather object containing weather information.
     */
    public static Weather getWeather() {
        // Weather API URL
        String apiUrl = "http://api.weatherapi.com/v1/current.json?key=d8a18be87c82447eb77182645232508&q=Albany,NY";

        try {
            // Create a URL object
            URL url = new URL(apiUrl);

            // Establish an HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer ");

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the API response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject current = jsonResponse.getJSONObject("current");
                JSONObject condition = current.getJSONObject("condition");

                // Extract weather data
                double tempFarhenhiet = current.getDouble("temp_f");
                double windMph = current.getDouble("wind_mph");
                int humidity = current.getInt("humidity");
                int uvIndex = current.getInt("uv");
                double precipIn = current.getDouble("precip_in");
                String conditionIcon = condition.getString("icon");
                String conditionText = condition.getString("text");

                return new Weather(tempFarhenhiet, windMph, humidity,
                        uvIndex, precipIn, conditionIcon, conditionText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
