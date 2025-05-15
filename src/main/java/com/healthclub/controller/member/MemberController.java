package com.healthclub.controller.member;

import com.healthclub.util.ViewLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MemberController {
    @FXML private StackPane mainContent;

    @FXML
    public void initialize() {
        System.out.println("MemberController initialized");
    }

    public void loadDashboardView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/member/MemberDashboard.fxml");
    }

    public void loadViewSubscriptionView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/member/ViewSubscription.fxml");
    }

    public void loadViewPlanView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/member/ViewPlan.fxml");
    }

    public void loadNotificationsView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/member/ViewNotifications.fxml");
    }

    public void loadMessagesView () {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/member/ViewMessages.fxml");
    }

    public void loadProfileView() {
        ViewLoaderUtil.loadView(mainContent, "/com/healthclub/Profile.fxml");
    }
}