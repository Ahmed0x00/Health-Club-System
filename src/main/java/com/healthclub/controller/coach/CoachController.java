package com.healthclub.controller.coach;

import com.healthclub.util.ViewLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class CoachController {
    @FXML private StackPane mainContent;

    @FXML
    public void initialize() {
        System.out.println("CoachController initialized");
    }

    public void loadDashboardView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/coach/CoachDashboard.fxml");
    }

    public void loadManagePlansView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/coach/ManagePlans.fxml");
    }

    public void loadSendMessagesView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/coach/SendMessages.fxml");
    }

    public void loadProfileView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/Profile.fxml");
    }
}