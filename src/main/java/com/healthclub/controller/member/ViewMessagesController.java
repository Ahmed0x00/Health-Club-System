package com.healthclub.controller.member;

import com.healthclub.model.Message;
import com.healthclub.util.MessageUtil;
import com.healthclub.util.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewMessagesController {
    @FXML private TableView<Message> messageTable;
    @FXML private TableColumn<Message, String> senderColumn;
    @FXML private TableColumn<Message, String> contentColumn;
    @FXML private TableColumn<Message, String> timestampColumn;

    @FXML
    public void initialize() {
        senderColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty("Coach"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        String memberId = UserUtil.getLoggedInUser().getId();
        messageTable.getItems().addAll(MessageUtil.getMessagesByRecipient(memberId));
    }
}