package com.healthclub.controller.admin;

import com.healthclub.model.Billing;
import com.healthclub.model.Member;
import com.healthclub.util.BillingUtil;
import com.healthclub.util.LoggerUtil;
import com.healthclub.util.MemberUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.scene.layout.VBox;

import java.util.List;

public class ManageBillingController {
    @FXML private TableView<Billing> billingTable;
    @FXML private TableColumn<Billing, String> memberColumn;
    @FXML private TableColumn<Billing, String> dateAddedColumn;
    @FXML private TableColumn<Billing, Double> amountColumn;
    @FXML private TableColumn<Billing, String> expiryDateColumn;

    @FXML
    public void initialize() {
        // Set up table columns
        memberColumn.setCellValueFactory(cellData -> {
            Billing billing = cellData.getValue();
            Member member = MemberUtil.getById(billing.getMemberId());
            return new javafx.beans.property.SimpleStringProperty(member != null ? member.getName() : "Unknown");
        });
        dateAddedColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        loadBillingRecords();
    }

    private void loadBillingRecords() {
        billingTable.getItems().clear();
        List<Billing> records = BillingUtil.getAllBillingRecords();
        billingTable.getItems().addAll(records);
    }

    @FXML
    public void onEditBilling() {
        Billing selected = billingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a billing record to edit.");
            alert.showAndWait();
            return;
        }
        showBillingDialog(selected);
    }

    private void showBillingDialog(Billing billing) {
        Dialog<Billing> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Billing");

        VBox content = new VBox(10);
        TextField amountField = new TextField(String.valueOf(billing.getAmount()));
        content.getChildren().addAll(
                new Label("Amount ($):"), amountField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());

                    if (amount <= 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Amount must be positive.");
                        alert.showAndWait();
                        return null;
                    }

                    billing.setAmount(amount);
                    BillingUtil.updateBilling(billing);
                    LoggerUtil.getLogger().info("Updated billing for member: " + billing.getMemberId());
                    return billing;
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Amount must be a valid number.");
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> loadBillingRecords());
    }
}