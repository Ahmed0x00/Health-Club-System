package com.healthclub.controller;

import com.healthclub.model.User;
import com.healthclub.model.User.Role;
import com.healthclub.util.LoggerUtil;
import com.healthclub.util.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        User user = UserUtil.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            LoggerUtil.getLogger().info("Login successful: " + username);
            UserUtil.setLoggedInUser(user);

            try {
                Role role = user.getRole();
                String view = switch (role) {
                    case ADMIN -> "/com/healthclub/admin/AdminDashboard.fxml";
                    case COACH -> "/com/healthclub/coach/CoachDashboard.fxml";
                    case MEMBER -> "/com/healthclub/member/MemberDashboard.fxml";
                };

                LoggerUtil.getLogger().info("Attempting to load FXML: " + view);
                java.net.URL resourceUrl = getClass().getResource(view);
                if (resourceUrl == null) {
                    throw new IOException("Resource not found: " + view);
                }
                Parent root = FXMLLoader.load(resourceUrl);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                LoggerUtil.getLogger().severe("Failed to load view: " + e.getMessage());
                e.printStackTrace();
                errorLabel.setText("UI error. Check logs.");
            }

        } else {
            LoggerUtil.getLogger().warning("Login failed for user: " + username);
            errorLabel.setText("Invalid username or password.");
        }
    }
}