package com.healthclub.controller.admin;

import com.healthclub.model.Coach;
import com.healthclub.model.User;
import com.healthclub.util.CoachUtil;
import com.healthclub.util.LoggerUtil;
import com.healthclub.util.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ManageCoachesController {
    @FXML private TextField searchField;
    @FXML private GridPane coachGrid;
    private static final int COLUMNS = 4;
    private List<Coach> allCoaches;

    @FXML
    public void initialize() {
        allCoaches = CoachUtil.getAllCoaches();
        loadCoaches(allCoaches);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterCoaches(newVal.trim()));
    }

    private void loadCoaches(List<Coach> coaches) {
        coachGrid.getChildren().clear();
        int col = 0;
        int row = 0;

        for (Coach coach : coaches) {
            VBox card = createCoachCard(coach);
            coachGrid.add(card, col, row);
            col++;
            if (col >= COLUMNS) {
                col = 0;
                row++;
            }
        }
    }

    private void filterCoaches(String query) {
        if (query.isEmpty()) {
            loadCoaches(allCoaches);
        } else {
            String lowerQuery = query.toLowerCase();
            List<Coach> filtered = allCoaches.stream()
                    .filter(coach -> coach.getName() != null && coach.getName().toLowerCase().contains(lowerQuery))
                    .collect(Collectors.toList());
            loadCoaches(filtered);
        }
    }

    private VBox createCoachCard(Coach coach) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10; -fx-background-color: #f2f2f2;");
        card.setPrefWidth(200);

        Label nameLabel = new Label("Name: " + (coach.getName() != null ? coach.getName() : "None"));
        Label phoneLabel = new Label("Phone: " + (coach.getPhoneNumber() != null ? coach.getPhoneNumber() : "None"));
        Label specializationLabel = new Label("Specialization: " + (coach.getSpecialization() != null ? coach.getSpecialization() : "None"));

        Button editBtn = new Button("Edit");
        editBtn.setOnAction(e -> showCoachDialog(coach, true));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            CoachUtil.deleteCoachById(coach.getId());
            allCoaches = CoachUtil.getAllCoaches();
            filterCoaches(searchField.getText().trim());
            LoggerUtil.getLogger().info("Deleted coach: " + (coach.getName() != null ? coach.getName() : "ID " + coach.getId()));
        });

        card.getChildren().addAll(nameLabel, phoneLabel, specializationLabel, editBtn, deleteBtn);
        return card;
    }

    @FXML
    public void onAddCoach() {
        showCoachDialog(new Coach(UUID.randomUUID().toString(), "", null, null), false);
    }

    private void showCoachDialog(Coach coach, boolean editing) {
        Dialog<Coach> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(editing ? "Edit Coach" : "Add Coach");

        VBox content = new VBox(10);
        TextField nameField = new TextField(coach.getName() != null ? coach.getName() : "");
        TextField phoneField = new TextField(coach.getPhoneNumber() != null ? coach.getPhoneNumber() : "");
        TextField specializationField = new TextField(coach.getSpecialization() != null ? coach.getSpecialization() : "");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Load username for editing
        if (editing) {
            User user = UserUtil.getById(coach.getId());
            if (user != null) {
                usernameField.setText(user.getUsername());
            }
        }

        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Phone Number:"), phoneField,
                new Label("Specialization:"), specializationField,
                new Label("Username:"), usernameField
        );
        if (!editing) {
            content.getChildren().addAll(new Label("Password:"), passwordField);
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                String name = nameField.getText().trim();
                String phoneNumber = phoneField.getText().trim();
                String specialization = specializationField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();

                if (name.isEmpty() || username.isEmpty() || (!editing && password.isEmpty())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Name, username, and password (for new coaches) are required.");
                    alert.showAndWait();
                    return null;
                }

                coach.setName(name);
                coach.setPhoneNumber(phoneNumber.isEmpty() ? null : phoneNumber);
                coach.setSpecialization(specialization.isEmpty() ? null : specialization);

                if (editing) {
                    if (CoachUtil.updateCoach(coach, username)) {
                        LoggerUtil.getLogger().info("Updated coach: " + name);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Username already exists or invalid input.");
                        alert.showAndWait();
                        return null;
                    }
                } else {
                    if (CoachUtil.addCoach(coach, username, password)) {
                        LoggerUtil.getLogger().info("Added coach: " + name);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Username already exists or invalid input.");
                        alert.showAndWait();
                        return null;
                    }
                }
                return coach;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            allCoaches = CoachUtil.getAllCoaches();
            filterCoaches(searchField.getText().trim());
        });
    }
}