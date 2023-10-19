package com.example.demo;
/**
 * Author: Rahul
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class NightLifeController extends HotelBookingController {

    @FXML
    private Button nextButtonn,previousButtonn,addNightLifeInfo,deleteNightLifeInfo;
    @FXML
    private TextField name;
    @FXML
    private TextField description;
    @FXML
    private TextField imageUrl;
    @FXML
    private TextField moreInfoUrl;
    @FXML
    private TextField namee;
    private int currentNightLifeIndex = 0;

    @FXML
    private ImageView nightLifeImage1,nightLifeImage2,nightLifeImage3;
    @FXML
    private DialogPane nightLifedescriptionPane1,nightLifedescriptionPane2,nightLifedescriptionPane3;
    @FXML
    private Hyperlink nightLifeLink1,nightLifeLink2,nightLifeLink3;

    /**
     * Show NightLife information on the screen.
     */
    public void show() {
        try {
            String emailId = HotelBooking.getInstance().getEmailId();
            HotelBooking hotelBooking=new HotelBooking();
            int roleId = hotelBooking.getRoleDetails(emailId);
            if(roleId==2){
                addNightLifeInfo.setVisible(true);
                deleteNightLifeInfo.setVisible(true);
            }
            ObservableList<NightLife> nightLifeItems = FXCollections.observableArrayList(NightLife.getNightlifeInfo());

            // Initialize the dialog panes and images for the first news article
            populateNightLifePanes(nightLifeItems, currentNightLifeIndex);

            // Add an event handler for the "Next" button
            nextButtonn.setOnAction(event -> {
                if (currentNightLifeIndex + 1 < nightLifeItems.size() - 2) {
                    currentNightLifeIndex++;
                    populateNightLifePanes(nightLifeItems, currentNightLifeIndex);
                }
            });
            previousButtonn.setOnAction(event -> {
                if (currentNightLifeIndex - 1 < nightLifeItems.size() - 2) {
                    if (currentNightLifeIndex == 0) {
                        currentNightLifeIndex = 1;
                    }
                    currentNightLifeIndex--;
                    populateNightLifePanes(nightLifeItems, currentNightLifeIndex);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the ImageView for the specified index.
     *
     * @param index The index of the ImageView.
     * @return The ImageView for the specified index.
     */
    public ImageView getNightLifeImagePaneForIndex(int index) {
        return switch (index) {
            case 0 -> nightLifeImage1;
            case 1 -> nightLifeImage2;
            case 2 -> nightLifeImage3;
            default ->
                    // Handle any other index as needed
                    null;
        };
    }

    /**
     * Get the DialogPane for the specified index.
     *
     * @param index The index of the DialogPane.
     * @return The DialogPane for the specified index.
     */
    public DialogPane getNightLifeDescriptionPaneForIndex(int index) {
        return switch (index) {
            case 0 -> nightLifedescriptionPane1;
            case 1 -> nightLifedescriptionPane2;
            case 2 -> nightLifedescriptionPane3;
            default ->
                    // Handle any other index as needed
                    null;
        };
    }

    /**
     * Get the Hyperlink for the specified index.
     *
     * @param index The index of the Hyperlink.
     * @return The Hyperlink for the specified index.
     */
    public Hyperlink getNightLifeLinkForIndex(int index) {
        return switch (index) {
            case 0 -> nightLifeLink1;
            case 1 -> nightLifeLink2;
            case 2 -> nightLifeLink3;
            // Add more cases if you have more Hyperlink fields in your layout
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        };
    }

    /**
     * Create a DialogPane for adding NightLife information.
     *
     * @return The created DialogPane.
     */
    private DialogPane addNightLifeDialogContent() {
        DialogPane dialogPane = new DialogPane();
        this.name=new TextField();
        this.description=new TextField();
        this.imageUrl=new TextField();
        this.moreInfoUrl=new TextField();
        // Customize the layout of the dialog content
        VBox content = new VBox(10); // Vertical layout with spacing
        content.getChildren().addAll(new javafx.scene.control.Label("Enter Night Life Name:"),name,new javafx.scene.control.Label("Enter Night Life address and description:"), description,new javafx.scene.control.Label("Enter Image URL:"),imageUrl,new javafx.scene.control.Label("Enter Website URL:"), moreInfoUrl);
        dialogPane.setContent(content);
        return dialogPane;
    }

    /**
     * Create a DialogPane for deleting NightLife information.
     *
     * @return The created DialogPane.
     */
    private DialogPane deleteNightLifeDialogContent() {
        DialogPane dialogPane = new DialogPane();
        this.namee=new TextField();
        // Customize the layout of the dialog content
        VBox content = new VBox(10); // Vertical layout with spacing
        content.getChildren().addAll(new javafx.scene.control.Label("Enter Night Life Title Name:"),namee);
        dialogPane.setContent(content);
        return dialogPane;
    }

    /**
     * Handle the action of adding NightLife information.
     */
    @FXML
    private void addNightLife() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Night Life Information");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(addNightLifeDialogContent());
        ButtonType addButtonType = new ButtonType("Add");
        dialogPane.getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                // Get the selected check-in and check-out dates from the DatePicker controls
                String nightLifename = name.getText();
                String nightLifedescription = description.getText();
                String nightLifeimageUrl = imageUrl.getText();
                String nightLifeWebsiteUrl = moreInfoUrl.getText();
                boolean querySuccess=false;
                String sql = "INSERT INTO nightlife (name, description, imageUrl,moreInfoUrl) VALUES ( ?, ?, ?, ?)";
                try (Connection connection = DBConn.connectDB()) {
                    assert connection != null;
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                        preparedStatement.setString(1, nightLifename);
                        preparedStatement.setString(2, nightLifedescription);
                        preparedStatement.setString(3, nightLifeimageUrl);
                        preparedStatement.setString(4, nightLifeWebsiteUrl);
                        int rowsAffected= preparedStatement.executeUpdate();
                       if(rowsAffected > 0){
                       querySuccess=true;
                       }
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
                if (querySuccess) {
                    // Show a success message using an Alert
                    showAlert(Alert.AlertType.INFORMATION, "Action Success",
                            "Night Life information added successfully!");
                } else {
                    // Show an error message using an Alert
                    showAlert(Alert.AlertType.ERROR, "Action Error", "Action failed");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    /**
     * Handle the action of deleting NightLife information.
     */
    @FXML
    private void deleteNightLife() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Delete Night Life Information");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(deleteNightLifeDialogContent());
        ButtonType addButtonType = new ButtonType("Delete");
        dialogPane.getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                // Get the selected check-in and check-out dates from the DatePicker controls
                String nightLifename = namee.getText();
                String sql = "delete from nightlife where name=?";
                try (Connection connection = DBConn.connectDB()) {
                    assert connection != null;
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                        preparedStatement.setString(1, nightLifename);

                        int rowsAffected = preparedStatement.executeUpdate();
                        if(rowsAffected > 0){
                            showAlert(Alert.AlertType.INFORMATION, "Query Success",
                                    "Night Life information deleted successfully!");
                        }
                        else{
                            showAlert(Alert.AlertType.INFORMATION, "Action failed",
                                    "Action Failed!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    /**
     * Populate the NightLife panes with information.
     *
     * @param nightLifeItems The list of NightLife items to populate.
     * @param startIndex     The start index for populating the panes.
     */
    private void populateNightLifePanes(ObservableList<NightLife> nightLifeItems, int startIndex) {
        int nightLifePerPage = 3;
        for (int i = 0; i < nightLifePerPage; i++) {
            int nightLifeIndex = startIndex + i;
            if (nightLifeIndex < nightLifeItems.size()) {
                NightLife nightLife = nightLifeItems.get(nightLifeIndex);
                ImageView imageView = getNightLifeImagePaneForIndex(i);

                DialogPane descriptionPane = getNightLifeDescriptionPaneForIndex(i);

                try {
                    if (Objects.equals(nightLife.getImageUrl(), "null")) {
                        Image image = new Image("file:src/main/resources/com/example/images/MicrosoftTeams-image.png");
                        imageView.setImage(image);
                    } else {
                        Image image = new Image(nightLife.getImageUrl());
                        imageView.setImage(image);
                    }

                    // Center the image within the ImageView
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(255);
                    imageView.setFitHeight(150);

                } catch (IllegalArgumentException e) {
                    // Handle the case where the image URL is invalid or not found
                    // Set a placeholder image or display an error message
                    imageView.setImage(null); // Set to a placeholder or null
                }
                Hyperlink newsLink = getNightLifeLinkForIndex(i);
                newsLink.setText("Go to Source");
                newsLink.setUnderline(true);
                newsLink.setOnAction(event -> {
                    // Open the URL in a web browser when the hyperlink is clicked
                    String url = nightLife.getMoreInfoUrl();
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
                descriptionPane.setContentText(nightLife.getDescription());
                descriptionPane.setHeaderText(nightLife.getName());
            } else {
                // If there are no more news articles, clear the remaining panes
                ImageView imageView = getNightLifeImagePaneForIndex(i);
                DialogPane descriptionPane = getNightLifeDescriptionPaneForIndex(i);
                imageView.setImage(null);
                descriptionPane.setContentText("");
                descriptionPane.setHeaderText("");

            }
        }
    }
}
