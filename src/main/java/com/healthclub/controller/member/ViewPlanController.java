package com.healthclub.controller.member;

import com.healthclub.model.Member;
import com.healthclub.model.Plan;
import com.healthclub.util.PlanUtil;
import com.healthclub.util.MemberUtil;
import com.healthclub.util.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.time.LocalDate;

public class ViewPlanController {
    @FXML private DatePicker datePicker;
    @FXML private Label workoutPlanLabel;
    @FXML private Label timeSlotLabel;

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> loadPlanForDate());
        loadPlanForDate();
    }

    private void loadPlanForDate() {
        String memberId = UserUtil.getLoggedInUser().getId();
        Member member = MemberUtil.getById(memberId);
        if (member != null && datePicker.getValue() != null) {
            String date = datePicker.getValue().toString();
            Plan plan = PlanUtil.getPlanByCoachIdAndDate(member.getCoachId(), date);
            if (plan != null) {
                workoutPlanLabel.setText(plan.getWorkoutPlan());
                timeSlotLabel.setText(plan.getTimeSlot());
            } else {
                workoutPlanLabel.setText("No plan assigned for this date.");
                timeSlotLabel.setText("No time slot assigned.");
            }
        } else {
            workoutPlanLabel.setText("No plan assigned.");
            timeSlotLabel.setText("No time slot assigned.");
        }
    }
}