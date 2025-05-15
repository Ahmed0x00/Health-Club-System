package com.healthclub.controller;

import com.healthclub.model.User;
import com.healthclub.util.LoggerUtil;
import com.healthclub.util.UserUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class ProfileController {
    @FXML private TextField idField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private StackPane mainContent;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = UserUtil.getLoggedInUser();
        if (currentUser != null) {
            idField.setText(currentUser.getId());
            idField.setEditable(false);
            usernameField.setText(currentUser.getUsername());
            passwordField.setText(currentUser.getPassword());
        } else {
            errorLabel.setText("No user logged in.");
            LoggerUtil.getLogger().warning("Profile accessed with no logged-in user.");
        }
    }

    @FXML
    public void handleSave() {
        if (currentUser == null) {
            errorLabel.setText("No user logged in.");
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        currentUser.setUsername(username);
        currentUser.setPassword(password);

        if (UserUtil.updateUser(currentUser)) {
            LoggerUtil.getLogger().info("Profile updated for user: " + username);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Profile updated successfully.");
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.showAndWait();
            Platform.exit(); // Exit the application after showing the alert
        } else {
            errorLabel.setText("Invalid input or username already exists.");
            LoggerUtil.getLogger().warning("Profile update failed for user: " + username);
        }
    }
}