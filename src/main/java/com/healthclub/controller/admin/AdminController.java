package com.healthclub.controller.admin;

import com.healthclub.util.ViewLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class AdminController {
    @FXML private StackPane mainContent;

    @FXML
    public void initialize() {
        System.out.println("AdminController initialized");
    }

    public void loadDashboardView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/admin/AdminDashboard.fxml");
    }

    public void loadManageCoachesView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/admin/ManageCoaches.fxml");
    }

    public void loadManageMembersView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/admin/ManageMembers.fxml");
    }

    public void loadManageBillingView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/admin/ManageBilling.fxml");
    }

    public void loadGenerateReportsView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/admin/GenerateReports.fxml");
    }

    public void loadNotificationsView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/admin/ManageNotifications.fxml");
    }

    public void loadProfileView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/Profile.fxml");
    }
}