package com.example.demo;
/**
 * Author: News
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents news articles retrieved from an external API.
 */
public class News {
    private final String title;
    private final String description;
    private final String url;
    private final String img_url;

    // List to store news articles
    private static final List<News> newsList = new ArrayList<>();

    /**
     * Constructor to create a News object.
     *
     * @param title       The title of the news article.
     * @param description The description of the news article.
     * @param url         The URL of the news article.
     * @param img_url     The URL of the news article's image.
     */
    public News(String title, String description, String url, String img_url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.img_url = img_url;
    }

    /**
     * Get the title of the news article.
     *
     * @return The title of the news article.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the description of the news article.
     *
     * @return The description of the news article.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the URL of the news article.
     *
     * @return The URL of the news article.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Get the URL of the news article's image.
     *
     * @return The URL of the news article's image.
     */
    public String getImg_url() {
        return this.img_url;
    }

    /**
     * Retrieve a list of news articles from an external API.
     *
     * @return A list of News objects representing news articles.
     */
    public static List<News> getNews() {
        // Define the API URL for news articles
        String apiUrl = "https://newsdata.io/api/1/news?apikey=pub_282524b67596732de8f9d3ecfb01a95de8781&q=NYC&country=us";

        try {
            // Create a URL object and open a connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer ");

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and parse the JSON response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray results = jsonResponse.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject article = results.getJSONObject(i);
                    String title = article.getString("title");
                    String description;

                    if (article.get("description") == null) {
                        description = "Description Unavailable: Please visit source link.";
                    } else {
                        description = article.get("description").toString();
                    }

                    String newsUrl = article.getString("link");
                    String newsImg = "null"; // Default value if "image_url" is not present

                    // Check if the "image_url" key exists in the JSON object
                    if (article.has("image_url") && !article.isNull("image_url")) {
                        newsImg = article.getString("image_url");
                    }

                    // Create a News object and add it to the list
                    newsList.add(new News(title, description, newsUrl, newsImg));
                }
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
