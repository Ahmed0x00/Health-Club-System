package com.healthclub.controller.coach;

import com.healthclub.model.Plan;
import com.healthclub.util.PlanUtil;
import com.healthclub.util.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ManagePlansController {
    @FXML private DatePicker datePicker;
    @FXML private TextArea workoutPlanArea;
    @FXML private TextField timeSlotField;
    @FXML private Button saveButton;

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
        saveButton.setDisable(true);
        workoutPlanArea.textProperty().addListener((obs, oldVal, newVal) -> updateSaveButton());
        timeSlotField.textProperty().addListener((obs, oldVal, newVal) -> updateSaveButton());
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> loadPlanForDate());
        loadPlanForDate();
    }

    private void updateSaveButton() {
        saveButton.setDisable(workoutPlanArea.getText().trim().isEmpty() ||
                timeSlotField.getText().trim().isEmpty() ||
                datePicker.getValue() == null);
    }

    private void loadPlanForDate() {
        String coachId = UserUtil.getLoggedInUser().getId();
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : null;
        if (date != null) {
            Plan plan = PlanUtil.getPlanByCoachIdAndDate(coachId, date);
            if (plan != null) {
                workoutPlanArea.setText(plan.getWorkoutPlan());
                timeSlotField.setText(plan.getTimeSlot());
            } else {
                workoutPlanArea.clear();
                timeSlotField.clear();
            }
        }
        updateSaveButton();
    }

    @FXML
    private void savePlan() {
        String coachId = UserUtil.getLoggedInUser().getId();
        String date = datePicker.getValue().toString();
        String workoutPlan = workoutPlanArea.getText().trim();
        String timeSlot = timeSlotField.getText().trim();

        if (PlanUtil.savePlan(coachId, date, workoutPlan, timeSlot)) {
            new Alert(Alert.AlertType.INFORMATION, "Plan saved successfully for " + date + ".").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save plan.").showAndWait();
        }
    }
}