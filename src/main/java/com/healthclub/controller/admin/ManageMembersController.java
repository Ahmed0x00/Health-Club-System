package com.healthclub.controller.admin;

import com.healthclub.model.Coach;
import com.healthclub.model.Member;
import com.healthclub.model.User;
import com.healthclub.util.CoachUtil;
import com.healthclub.util.LoggerUtil;
import com.healthclub.util.MemberUtil;
import com.healthclub.util.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ManageMembersController {
    @FXML private TextField searchField;
    @FXML private GridPane memberGrid;
    private static final int COLUMNS = 4;
    private List<Member> allMembers;

    @FXML
    public void initialize() {
        allMembers = MemberUtil.getAllMembers();
        loadMembers(allMembers);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterMembers(newVal.trim()));
    }

    private void loadMembers(List<Member> members) {
        memberGrid.getChildren().clear();
        int col = 0;
        int row = 0;

        for (Member member : members) {
            VBox card = createMemberCard(member);
            memberGrid.add(card, col, row);
            col++;
            if (col >= COLUMNS) {
                col = 0;
                row++;
            }
        }
    }

    private void filterMembers(String query) {
        if (query.isEmpty()) {
            loadMembers(allMembers);
        } else {
            String lowerQuery = query.toLowerCase();
            List<Member> filtered = allMembers.stream()
                    .filter(member -> member.getName() != null && member.getName().toLowerCase().contains(lowerQuery))
                    .collect(Collectors.toList());
            loadMembers(filtered);
        }
    }

    private VBox createMemberCard(Member member) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10; -fx-background-color: #f2f2f2;");
        card.setPrefWidth(200);

        Label nameLabel = new Label("Name: " + (member.getName() != null ? member.getName() : "None"));
        Label phoneLabel = new Label("Phone: " + (member.getPhoneNumber() != null ? member.getPhoneNumber() : "None"));
        Label emailLabel = new Label("Email: " + (member.getEmail() != null ? member.getEmail() : "None"));
        Label coachLabel = new Label("Coach: " + getCoachName(member.getCoachId()));
        Label expiryLabel = new Label("Expiry: " + (member.getExpiryDate() != null ? member.getExpiryDate() : "None"));

        Button editBtn = new Button("Edit");
        editBtn.setOnAction(e -> showMemberDialog(member, true));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            MemberUtil.deleteMemberById(member.getId());
            allMembers = MemberUtil.getAllMembers();
            filterMembers(searchField.getText().trim());
            LoggerUtil.getLogger().info("Deleted member: " + (member.getName() != null ? member.getName() : "ID " + member.getId()));
        });

        card.getChildren().addAll(nameLabel, phoneLabel, emailLabel, coachLabel, expiryLabel, editBtn, deleteBtn);
        return card;
    }

    private String getCoachName(String coachId) {
        Coach coach = CoachUtil.getById(coachId);
        return coach != null && coach.getName() != null ? coach.getName() : "Unknown";
    }

    @FXML
    public void onAddMember() {
        showMemberDialog(new Member(UUID.randomUUID().toString(), "", null, null, null, null), false);
    }

    private void showMemberDialog(Member member, boolean editing) {
        Dialog<Member> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(editing ? "Edit Member" : "Add Member");

        VBox content = new VBox(10);
        TextField nameField = new TextField(member.getName() != null ? member.getName() : "");
        TextField phoneField = new TextField(member.getPhoneNumber() != null ? member.getPhoneNumber() : "");
        TextField emailField = new TextField(member.getEmail() != null ? member.getEmail() : "");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        ComboBox<Coach> coachCombo = new ComboBox<>();
        coachCombo.setPromptText("Select a coach");
        ComboBox<String> durationCombo = new ComboBox<>();
        durationCombo.setPromptText("Select subscription duration");
        durationCombo.getItems().addAll("1 Month", "3 Months", "1 Year");

        // Load coaches
        List<Coach> coaches = CoachUtil.getAllCoaches();
        coachCombo.getItems().addAll(coaches);
        coachCombo.setCellFactory(lv -> new ListCell<Coach>() {
            @Override
            protected void updateItem(Coach coach, boolean empty) {
                super.updateItem(coach, empty);
                setText(empty || coach == null || coach.getName() == null ? "" : coach.getName());
            }
        });
        coachCombo.setButtonCell(new ListCell<Coach>() {
            @Override
            protected void updateItem(Coach coach, boolean empty) {
                super.updateItem(coach, empty);
                setText(empty || coach == null || coach.getName() == null ? "" : coach.getName());
            }
        });
        if (editing && member.getCoachId() != null) {
            Coach selectedCoach = CoachUtil.getById(member.getCoachId());
            if (selectedCoach != null) {
                coachCombo.setValue(selectedCoach);
            }
        }

        // Load username and duration for editing
        if (editing) {
            User user = UserUtil.getById(member.getId());
            if (user != null) {
                usernameField.setText(user.getUsername());
            }
            if (member.getExpiryDate() != null) {
                try {
                    LocalDate expiry = LocalDate.parse(member.getExpiryDate());
                    LocalDate now = LocalDate.now();
                    if (expiry.isAfter(now.plusYears(1).minusDays(1))) {
                        durationCombo.setValue("1 Year");
                    } else if (expiry.isAfter(now.plusMonths(3).minusDays(1))) {
                        durationCombo.setValue("3 Months");
                    } else if (expiry.isAfter(now.plusMonths(1).minusDays(1))) {
                        durationCombo.setValue("1 Month");
                    }
                } catch (Exception e) {
                    // Fallback to 1 Month if expiry date is invalid
                    durationCombo.setValue("1 Month");
                }
            }
        }

        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Phone Number:"), phoneField,
                new Label("Email:"), emailField,
                new Label("Username:"), usernameField,
                new Label("Coach:"), coachCombo,
                new Label("Subscription Duration:"), durationCombo
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
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = editing ? null : passwordField.getText().trim();
                Coach selectedCoach = coachCombo.getValue();
                String duration = durationCombo.getValue();

                if (name.isEmpty() || username.isEmpty() || selectedCoach == null || duration == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Name, username, coach, and subscription duration are required.");
                    alert.showAndWait();
                    return null;
                }
                if (!editing && (password == null || password.isEmpty())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password is required for new members.");
                    alert.showAndWait();
                    return null;
                }

                // Calculate expiry date
                LocalDate expiryDate;
                switch (duration) {
                    case "1 Month":
                        expiryDate = LocalDate.now().plusMonths(1);
                        break;
                    case "3 Months":
                        expiryDate = LocalDate.now().plusMonths(3);
                        break;
                    case "1 Year":
                        expiryDate = LocalDate.now().plusYears(1);
                        break;
                    default:
                        expiryDate = editing && member.getExpiryDate() != null ?
                                LocalDate.parse(member.getExpiryDate()) :
                                LocalDate.now().plusMonths(1);
                }

                member.setName(name);
                member.setPhoneNumber(phoneNumber.isEmpty() ? null : phoneNumber);
                member.setEmail(email.isEmpty() ? null : email);
                member.setCoachId(selectedCoach.getId());
                member.setExpiryDate(expiryDate.toString());

                if (editing) {
                    if (MemberUtil.updateMember(member, username)) {
                        LoggerUtil.getLogger().info("Updated member: " + name);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Username already exists, invalid expiry date, or other error.");
                        alert.showAndWait();
                        return null;
                    }
                } else {
                    if (MemberUtil.addMember(member, username, password)) {
                        LoggerUtil.getLogger().info("Added member: " + name);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Username already exists, invalid expiry date, or other error.");
                        alert.showAndWait();
                        return null;
                    }
                }
                return member;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            allMembers = MemberUtil.getAllMembers();
            filterMembers(searchField.getText().trim());
        });
    }
}