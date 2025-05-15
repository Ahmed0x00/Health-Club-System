package com.healthclub.controller.coach;

import com.healthclub.util.MessageUtil;
import com.healthclub.util.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SendMessagesController {
    @FXML private TextArea messageArea;
    @FXML private Button sendButton;

    @FXML
    public void initialize() {
        sendButton.setDisable(true);
        messageArea.textProperty().addListener((obs, oldVal, newVal) ->
                sendButton.setDisable(newVal.trim().isEmpty()));
    }

    @FXML
    private void sendMessage() {
        String content = messageArea.getText().trim();
        String senderId = UserUtil.getLoggedInUser().getId();

        if (MessageUtil.sendMessage(senderId, content)) {
            new Alert(Alert.AlertType.INFORMATION, "Message sent to all members successfully.").showAndWait();
            messageArea.clear();
            sendButton.setDisable(true);
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to send message. No assigned members found.").showAndWait();
        }
    }
}