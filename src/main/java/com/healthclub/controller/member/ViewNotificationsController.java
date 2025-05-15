package com.healthclub.controller.member;

import com.healthclub.model.Notification;
import com.healthclub.util.MemberUtil;
import com.healthclub.util.NotificationUtil;
import com.healthclub.util.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewNotificationsController {
    @FXML private Label messageLabel;
    @FXML private Label dateLabel;

    @FXML
    public void initialize() {
        // Run expiry check on startup
        MemberUtil.checkExpiries();

        String memberId = UserUtil.getLoggedInUser().getId();
        Notification notification = NotificationUtil.getByMemberId(memberId).stream().findFirst().orElse(null);
        if (notification != null) {
            messageLabel.setText(notification.getMessage());
            dateLabel.setText("Date: " + notification.getDate());
        } else {
            messageLabel.setText("No subscription expiry notifications.");
            dateLabel.setText("");
        }
    }
}