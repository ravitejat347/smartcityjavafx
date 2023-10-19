package com.example.demo;
/**
 * Author: Maaz
 */

import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import java.net.URL;

public class MapController {

    /**
     * Display a map in a WebView.
     *
     * @param webview The WebView in which to display the map.
     */
    public static void showMap(WebView webview) {
        WebEngine webEngine = webview.getEngine();
        webEngine.setJavaScriptEnabled(true);

        // Load the HTML map from a local resource
        final URL htmlFilePath = MapController.class.getResource("map.html");
        assert htmlFilePath != null;
        webEngine.load(htmlFilePath.toExternalForm());

        // Set an error handler to display a dialog for any web page loading errors
        webEngine.setOnError((WebErrorEvent event) -> showErrorDialog(event.getMessage()));
    }

    /**
     * Display an error dialog with the given error message.
     *
     * @param errorMessage The error message to display in the dialog.
     */
    private static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("WebView Error");
        alert.setHeaderText("An error occurred while loading the web page.");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}

