package com.healthclub.controller.admin;

import com.healthclub.model.Member;
import com.healthclub.model.Billing;
import com.healthclub.util.LoggerUtil;
import com.healthclub.util.MemberUtil;
import com.healthclub.util.ReportUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class GenerateReportsController {
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private TextField daysField;
    @FXML private TableView<Object> reportTable;
    @FXML private TableColumn<Object, String> col1;
    @FXML private TableColumn<Object, String> col2;
    @FXML private TableColumn<Object, String> col3;
    @FXML private TableColumn<Object, String> col4;
    @FXML private TableColumn<Object, String> col5;
    @FXML private Button exportButton;

    @FXML
    public void initialize() {
        reportTypeCombo.getItems().addAll("Member Summary", "Expiring Subscriptions", "Billing Summary");
        reportTypeCombo.setValue("Member Summary");
        daysField.setText("30");
        daysField.setDisable(true);
        setupTableColumns();
        generateReport();

        reportTypeCombo.setOnAction(e -> {
            daysField.setDisable(!reportTypeCombo.getValue().equals("Expiring Subscriptions"));
            generateReport();
        });
        daysField.textProperty().addListener((obs, oldVal, newVal) -> generateReport());
    }

    private void setupTableColumns() {
        col1.setCellValueFactory(cellData -> {
            Object obj = cellData.getValue();
            if (obj instanceof Member m) return new javafx.beans.property.SimpleStringProperty(m.getId());
            if (obj instanceof Billing b) return new javafx.beans.property.SimpleStringProperty(b.getMemberId());
            return new javafx.beans.property.SimpleStringProperty("");
        });
        col2.setCellValueFactory(cellData -> {
            Object obj = cellData.getValue();
            if (obj instanceof Member m) return new javafx.beans.property.SimpleStringProperty(m.getName());
            if (obj instanceof Billing b) {
                Member m = MemberUtil.getById(b.getMemberId());
                return new javafx.beans.property.SimpleStringProperty(m != null ? m.getName() : "Unknown");
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        col3.setCellValueFactory(cellData -> {
            Object obj = cellData.getValue();
            if (obj instanceof Member m) return new javafx.beans.property.SimpleStringProperty(m.getEmail());
            if (obj instanceof Billing b) return new javafx.beans.property.SimpleStringProperty(String.format("%.2f", b.getAmount()));
            return new javafx.beans.property.SimpleStringProperty("");
        });
        col4.setCellValueFactory(cellData -> {
            Object obj = cellData.getValue();
            if (obj instanceof Member m) return new javafx.beans.property.SimpleStringProperty(m.getName());
            if (obj instanceof Billing b) return new javafx.beans.property.SimpleStringProperty(b.getDateAdded());
            return new javafx.beans.property.SimpleStringProperty("");
        });
        col5.setCellValueFactory(cellData -> {
            Object obj = cellData.getValue();
            if (obj instanceof Member m) return new javafx.beans.property.SimpleStringProperty(m.getExpiryDate());
            if (obj instanceof Billing b) return new javafx.beans.property.SimpleStringProperty(b.getExpiryDate());
            return new javafx.beans.property.SimpleStringProperty("");
        });
    }

    @FXML
    private void generateReport() {
        reportTable.getItems().clear();
        String reportType = reportTypeCombo.getValue();
        List<?> data;
        switch (reportType) {
            case "Member Summary":
                data = ReportUtil.generateMemberSummary();
                col1.setText("ID"); col2.setText("Name"); col3.setText("Email"); col4.setText("Coach Name"); col5.setText("Expiry Date");
                break;
            case "Expiring Subscriptions":
                try {
                    int days = Integer.parseInt(daysField.getText().trim());
                    if (days <= 0) throw new NumberFormatException();
                    data = ReportUtil.generateExpiringSubscriptions(days);
                    col1.setText("ID"); col2.setText("Name"); col3.setText("Email"); col4.setText("Coach ID"); col5.setText("Expiry Date");
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Please enter a valid number of days.").showAndWait();
                    return;
                }
                break;
            case "Billing Summary":
                data = ReportUtil.generateBillingSummary();
                col1.setText("Member ID"); col2.setText("Member Name"); col3.setText("Amount"); col4.setText("Subscription Date"); col5.setText("Expiry Date");
                break;
            default:
                return;
        }
        reportTable.getItems().addAll(data);
        exportButton.setDisable(data.isEmpty());
    }

    @FXML
    private void exportReport() {
        String reportType = reportTypeCombo.getValue();
        List<?> data = reportTable.getItems();
        if (data.isEmpty()) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(reportType.replace(" ", "_") + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(reportTable.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(ReportUtil.exportToCsv(data, reportType));
                new Alert(Alert.AlertType.INFORMATION, "Report exported successfully.").showAndWait();
            } catch (Exception e) {
                LoggerUtil.getLogger().severe("CSV Export Error: " + e.getMessage());
                new Alert(Alert.AlertType.ERROR, "Failed to export report.").showAndWait();
            }
        }
    }
}