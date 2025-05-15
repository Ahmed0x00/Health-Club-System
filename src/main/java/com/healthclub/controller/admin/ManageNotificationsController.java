package com.healthclub.controller.admin;

import com.healthclub.model.Notification;
import com.healthclub.model.Member;
import com.healthclub.util.MemberUtil;
import com.healthclub.util.NotificationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ManageNotificationsController {
    @FXML private TableView<Notification> notificationTable;
    @FXML private TableColumn<Notification, String> memberColumn;
    @FXML private TableColumn<Notification, String> messageColumn;
    @FXML private TableColumn<Notification, String> dateColumn;

    @FXML
    public void initialize() {
        // Run expiry check on startup
        MemberUtil.checkExpiries();

        // Set up table columns
        memberColumn.setCellValueFactory(cellData -> {
            Notification notification = cellData.getValue();
            Member member = MemberUtil.getById(notification.getMemberId());
            return new javafx.beans.property.SimpleStringProperty(member != null ? member.getName() : "Unknown");
        });
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadNotifications();
    }

    private void loadNotifications() {
        notificationTable.getItems().clear();
        List<Notification> notifications = NotificationUtil.getAllNotifications();
        notificationTable.getItems().addAll(notifications);
    }
}