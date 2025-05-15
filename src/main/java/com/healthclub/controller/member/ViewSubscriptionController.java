package com.healthclub.controller.member;

import com.healthclub.model.Member;
import com.healthclub.util.CoachUtil;
import com.healthclub.util.MemberUtil;
import com.healthclub.util.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class ViewSubscriptionController {
    @FXML private Label expiryLabel;
    @FXML private Label statusLabel;
    @FXML private Label coachLabel;

    @FXML
    public void initialize() {
        Member member = MemberUtil.getById(UserUtil.getLoggedInUser().getId());
        if (member != null && member.getExpiryDate() != null) {
            expiryLabel.setText("Expiry Date: " + member.getExpiryDate());
            boolean isActive = LocalDate.parse(member.getExpiryDate()).isAfter(LocalDate.now());
            statusLabel.setText("Status: " + (isActive ? "Active" : "Expired"));
            String coachName = CoachUtil.getById(member.getCoachId()) != null
                    ? CoachUtil.getById(member.getCoachId()).getName() : "Unknown";
            coachLabel.setText("Coach: " + coachName);
        } else {
            expiryLabel.setText("Expiry Date: None");
            statusLabel.setText("Status: No active subscription");
            coachLabel.setText("Coach: None");
        }
    }
}