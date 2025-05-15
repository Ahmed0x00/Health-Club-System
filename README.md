# Project Structure: Health Club Management System

```text
src
└── main
    ├── java
    │   ├── com
    │   │   └── healthclub
    │   │       ├── controller
    │   │       │   ├── admin
    │   │       │   │   ├── AdminController.java
    │   │       │   │   ├── GenerateReportsController.java
    │   │       │   │   ├── ManageBillingController.java
    │   │       │   │   ├── ManageCoachesController.java
    │   │       │   │   ├── ManageMembersController.java
    │   │       │   │   └── ManageNotificationsController.java
    │   │       │   ├── AuthController.java
    │   │       │   ├── coach
    │   │       │   │   ├── CoachController.java
    │   │       │   │   ├── ManagePlansController.java
    │   │       │   │   └── SendMessagesController.java
    │   │       │   ├── member
    │   │       │   │   ├── MemberController.java
    │   │       │   │   ├── ViewMessagesController.java
    │   │       │   │   ├── ViewNotificationsController.java
    │   │       │   │   ├── ViewPlanController.java
    │   │       │   │   └── ViewSubscriptionController.java
    │   │       │   └── ProfileController.java
    │   │       ├── Main.java
    │   │       ├── model
    │   │       │   ├── Billing.java
    │   │       │   ├── Coach.java
    │   │       │   ├── Member.java
    │   │       │   ├── Message.java
    │   │       │   ├── Notification.java
    │   │       │   ├── Plan.java
    │   │       │   └── User.java
    │   │       └── util
    │   │           ├── BillingUtil.java
    │   │           ├── CoachUtil.java
    │   │           ├── JsonUtil.java
    │   │           ├── LoggerUtil.java
    │   │           ├── MemberUtil.java
    │   │           ├── MessageUtil.java
    │   │           ├── NotificationUtil.java
    │   │           ├── PlanUtil.java
    │   │           ├── ReportUtil.java
    │   │           ├── UserUtil.java
    │   │           └── ViewLoaderUtil.java
    │   └── module-info.java
    └── resources
        ├── com
        │   └── healthclub
        │       ├── admin
        │       │   ├── AdminDashboard.fxml
        │       │   ├── AssignMembers.fxml
        │       │   ├── GenerateReports.fxml
        │       │   ├── ManageBilling.fxml
        │       │   ├── ManageCoaches.fxml
        │       │   ├── ManageMembers.fxml
        │       │   └── ManageNotifications.fxml
        │       ├── coach
        │       │   ├── CoachDashboard.fxml
        │       │   ├── ManagePlans.fxml
        │       │   └── SendMessages.fxml
        │       ├── login.fxml
        │       ├── member
        │       │   ├── MemberDashboard.fxml
        │       │   ├── ViewMessages.fxml
        │       │   ├── ViewNotifications.fxml
        │       │   ├── ViewPlan.fxml
        │       │   └── ViewSubscription.fxml
        │       └── Profile.fxml
        └── data
            ├── billing.json
            ├── coaches.json
            ├── members.json
            ├── messages.json
            ├── notifications.json
            ├── plans.json
            ├── users.json
```

## Overview

The **Health Club Management System** is a JavaFX-based application designed to manage a fitness club’s operations, including member and coach management, billing, notifications, fitness plans, messaging, and reporting. It supports three user roles—Admin, Coach, and Member—each with role-specific dashboards and functionalities. Data is stored in JSON files, with utility classes handling CRUD operations and JavaFX FXML files defining the UI. The system emphasizes usability, with features like search functionality for admins and validation for data integrity (e.g., MemberUtil.java).

## Models

These classes represent the data entities in the system, with fields, constructors, and getters/setters. All models are in src/main/java/com/healthclub/model/ and serialized to JSON files via JsonUtil.

1. **Billing.java**: Represents billing records for member subscriptions.
    
    - **Fields**: id (String, unique), memberId (String, references Member.id), amount (double, billing amount), date (String, billing date), status (String, e.g., "Paid", "Pending").
        
    - **Purpose**: Tracks payments for subscriptions, used by admins in ManageBillingController.java.
        
    - **Stored In**: billing.json.
        
2. **Coach.java**: Represents a coach in the system (provided in prior context).
    
    - **Fields**: id (String, unique), name (String), phoneNumber (String, nullable), specialization (String, nullable, e.g., "Yoga", "Strength Training").
        
    - **Purpose**: Stores coach details for assignment to members and plan creation.
        
    - **Stored In**: coaches.json.
        
3. **Member.java**: Represents a gym member (provided in prior context).
    
    - **Fields**: id (String, unique), name (String), phoneNumber (String, nullable), email (String, nullable), coachId (String, references Coach.id), expiryDate (String, subscription end date).
        
    - **Purpose**: Stores member details for subscription tracking and coach assignment.
        
    - **Stored In**: members.json.
        
4. **Message.java**: Represents messages sent by coaches to members.
    
    - **Fields**: id (String, unique), coachId (String, references Coach.id), memberId (String, references Member.id, nullable for group messages), content (String), date (String).
        
    - **Purpose**: Enables communication from coaches to members (e.g., announcements).
        
    - **Stored In**: messages.json.
        
5. **Notification.java**: Represents system-generated notifications (e.g., subscription expiry).
    
    - **Fields**: id (String, unique), memberId (String, references Member.id), message (String), date (String).
        
    - **Purpose**: Alerts members and admins about events like expiring subscriptions.
        
    - **Stored In**: notifications.json.
        
6. **Plan.java**: Represents a fitness plan created by a coach for a member.
    
    - **Fields**: id (String, unique), coachId (String, references Coach.id), memberId (String, references Member.id), title (String), description (String), startDate (String), endDate (String).
        
    - **Purpose**: Defines workout or diet plans assigned to members.
        
    - **Stored In**: plans.json.
        
7. **User.java**: Represents a generic user for authentication (provided in prior context).
    
    - **Fields**: id (String, unique), username (String, unique), password (String), role (enum: ADMIN, COACH, MEMBER).
        
    - **Purpose**: Handles authentication for all user types (admins, coaches, members), eliminating the need for a separate Admin model.
        
    - **Stored In**: users.json.
        

## Utils

Utility classes in src/main/java/com/healthclub/util/ handle data operations (CRUD), JSON serialization, logging, and UI loading. They interact with JSON files in src/main/resources/data/.

1. **BillingUtil.java**: Manages CRUD operations for billing records.
    
    - **Methods**: getAll, addBilling, updateBilling, deleteBilling, getByMemberId.
        
    - **Purpose**: Supports billing management in ManageBillingController.java.
        
    - **Interacts With**: billing.json.
        
2. **CoachUtil.java**: Manages CRUD operations for coaches (provided in prior context).
    
    - **Methods**: getAllCoaches, addCoach, updateCoach, deleteCoachById, getById.
        
    - **Purpose**: Handles coach data for ManageCoachesController.java.
        
    - **Interacts With**: coaches.json, users.json (via UserUtil for authentication).
        
3. **JsonUtil.java**: Provides generic JSON handling for all models.
    
    - **Methods**: getAll, add, edit, delete, getOne.
        
    - **Purpose**: Abstracts JSON serialization/deserialization, used by all *Util classes.
        
    - **Interacts With**: All JSON files.
        
4. **LoggerUtil.java**: Provides logging functionality.
    
    - **Methods**: getLogger (returns configured Logger).
        
    - **Purpose**: Logs operations (e.g., "Added member" in ManageMembersController.java).
        
    - **Interacts With**: System logs.
        
5. **MemberUtil.java**: Manages CRUD operations for members (provided in prior context).
    
    - **Methods**: getAllMembers, addMember, updateMember, deleteMemberById, getById, checkExpiries.
        
    - **Purpose**: Handles member data for ManageMembersController.java, including subscription expiry checks.
        
    - **Interacts With**: members.json, users.json, billing.json, notifications.json.
        
6. **MessageUtil.java**: Manages CRUD operations for messages.
    
    - **Methods**: getAll, addMessage, deleteMessage, getByMemberId, getByCoachId.
        
    - **Purpose**: Supports messaging in SendMessagesController.java and ViewMessagesController.java.
        
    - **Interacts With**: messages.json.
        
7. **NotificationUtil.java**: Manages CRUD operations for notifications.
    
    - **Methods**: getAll, addNotification, deleteByMemberId, getByMemberId.
        
    - **Purpose**: Generates and manages notifications (e.g., expiry alerts in MemberUtil.checkExpiries).
        
    - **Interacts With**: notifications.json.
        
8. **PlanUtil.java**: Manages CRUD operations for fitness plans.
    
    - **Methods**: getAll, addPlan, updatePlan, deletePlan, getByMemberId, getByCoachId.
        
    - **Purpose**: Supports plan management in ManagePlansController.java and ViewPlanController.java.
        
    - **Interacts With**: plans.json.
        
9. **ReportUtil.java**: Manages report generation and storage.
    
    - **Methods**: generateReport, getAll, getById, deleteReport.
        
    - **Purpose**: Creates reports (e.g., member activity) for GenerateReportsController.java.
        
    - **Interacts With**: No dedicated reports.json (may use members.json, billing.json for data).
        
10. **UserUtil.java**: Manages CRUD operations for users.
    
    - **Methods**: getAll, addUser, updateUser, deleteUserById, getById, findByUsername.
        
    - **Purpose**: Handles authentication and user management across all roles.
        
    - **Interacts With**: users.json.
        
11. **ViewLoaderUtil.java**: Loads FXML views for JavaFX.
    
    - **Methods**: loadView (loads FXML with controller).
        
    - **Purpose**: Transitions between views (e.g., from login.fxml to dashboards).
        
    - **Interacts With**: All FXML files.
        

## Views (FXML Files)

FXML files in src/main/resources/com/healthclub/ define the UI using JavaFX components (e.g., VBox, TextField, Button). Each is paired with a controller.

1. **admin/AdminDashboard.fxml**: Admin dashboard UI.
    
    - **Components**: Buttons for navigating to ManageMembers, ManageCoaches, ManageBilling, GenerateReports, ManageNotifications, AssignMembers.
        
    - **Purpose**: Central hub for admin tasks.
        
2. **admin/AssignMembers.fxml**: UI for assigning members to coaches.
    
    - **Components**: List of members, list of coaches, assignment controls.
        
    - **Purpose**: Manages coach-member relationships.
        
3. **admin/GenerateReports.fxml**: UI for generating and viewing reports.
    
    - **Components**: Report type selector, date range, generate button, report display.
        
    - **Purpose**: Creates reports on member activity, billing, etc.
        
4. **admin/ManageBilling.fxml**: UI for managing billing records.
    
    - **Components**: List/Table of bills, add/edit/delete controls.
        
    - **Purpose**: Tracks and updates billing status.
        
5. **admin/ManageCoaches.fxml**: UI for coach management
    
    - **Components**: Search TextField, GridPane with VBox cards (name, phone, specialization), Add/Edit/Delete buttons.
        
    - **Purpose**: Allows admins to add, update, delete, and search coaches by name.
        
6. **admin/ManageMembers.fxml**: UI for member management
    
    - **Components**: Search TextField, GridPane with VBox cards (name, phone, email, coach, expiry), Add/Edit/Delete buttons.
        
    - **Purpose**: Allows admins to add, update, delete, and search members by name.
        
7. **admin/ManageNotifications.fxml**: UI for managing notifications.
    
    - **Components**: List of notifications, delete controls.
        
    - **Purpose**: Allows admins to view and clear notifications.
        
8. **coach/CoachDashboard.fxml**: Coach dashboard UI.
    
    - **Components**: Buttons for navigating to ManagePlans, SendMessages.
        
    - **Purpose**: Central hub for coach tasks.
        
9. **coach/ManagePlans.fxml**: UI for managing fitness plans.
    
    - **Components**: List of plans, add/edit/delete controls, member selector.
        
    - **Purpose**: Allows coaches to create and assign plans.
        
10. **coach/SendMessages.fxml**: UI for sending messages.
    
    - **Components**: Message input, recipient selector (all members or specific), send button.
        
    - **Purpose**: Enables coaches to communicate with members.
        
11. **member/MemberDashboard.fxml**: Member dashboard UI.
    
    - **Components**: Buttons for navigating to ViewPlan, ViewSubscription, ViewMessages, ViewNotifications, Profile.
        
    - **Purpose**: Central hub for member tasks.
        
12. **member/ViewMessages.fxml**: UI for viewing coach messages.
    
    - **Components**: List of messages (content, date, coach).
        
    - **Purpose**: Displays messages sent to the member.
        
13. **member/ViewNotifications.fxml**: UI for viewing notifications.
    
    - **Components**: List of notifications (message, date).
        
    - **Purpose**: Shows system alerts (e.g., subscription expiry).
        
14. **member/ViewPlan.fxml**: UI for viewing fitness plans.
    
    - **Components**: Plan details (title, description, dates).
        
    - **Purpose**: Displays the member’s assigned plan.
        
15. **member/ViewSubscription.fxml**: UI for viewing subscription details.
    
    - **Components**: Subscription details (start date, end date, status).
        
    - **Purpose**: Shows the member’s subscription status.
        
16. **Profile.fxml**: UI for viewing/editing user profile.
    
    - **Components**: Fields for username, name, email, phone, password update.
        
    - **Purpose**: Allows any user to update personal info.
        
17. **login.fxml**: Login screen UI.
    
    - **Components**: Username and password fields, login button.
        
    - **Purpose**: Authenticates users and directs to role-specific dashboards.
        

## Controllers

Controllers in src/main/java/com/healthclub/controller/ handle the logic for FXML views, interacting with models and utilities.

1. **admin/AdminController.java**: Controls AdminDashboard.fxml.
    
    - **Functions**: Handles navigation to admin views (e.g., ManageMembers.fxml).
        
    - **Interacts With**: ViewLoaderUtil.
        
2. **admin/GenerateReportsController.java**: Controls GenerateReports.fxml.
    
    - **Functions**: Generates reports using ReportUtil, displays results.
        
    - **Interacts With**: ReportUtil, MemberUtil, BillingUtil.
        
3. **admin/ManageBillingController.java**: Controls ManageBilling.fxml.
    
    - **Functions**: Adds, updates, deletes billing records via BillingUtil.
        
    - **Interacts With**: BillingUtil, MemberUtil.
        
4. **admin/ManageCoachesController.java**: Controls ManageCoaches.fxml
    
    - **Functions**: Displays coach cards, supports add/edit/delete, searches by name using TextField.
        
    - **Interacts With**: CoachUtil, UserUtil, LoggerUtil.
        
    - **Details**: Uses GridPane with VBox cards, filters via filterCoaches method.
        
5. **admin/ManageMembersController.java**: Controls ManageMembers.fxml
    
    - **Functions**: Displays member cards, supports add/edit/delete, searches by name using TextField, validates inputs (e.g., no password required for editing).
        
    - **Interacts With**: MemberUtil, CoachUtil, UserUtil, LoggerUtil.
        
    - **Details**: Uses GridPane with VBox cards, filters via filterMembers method, fixed dialog validation for editing 
        
6. **admin/ManageNotificationsController.java**: Controls ManageNotifications.fxml.
    
    - **Functions**: Displays and deletes notifications.
        
    - **Interacts With**: NotificationUtil, MemberUtil.
        
7. **AuthController.java**: Controls login.fxml.
    
    - **Functions**: Authenticates users via UserUtil, directs to role-specific dashboards (AdminDashboard.fxml, CoachDashboard.fxml, MemberDashboard.fxml).
        
    - **Interacts With**: UserUtil, ViewLoaderUtil.
        
8. **coach/CoachController.java**: Controls CoachDashboard.fxml.
    
    - **Functions**: Handles navigation to coach views (e.g., ManagePlans.fxml).
        
    - **Interacts With**: ViewLoaderUtil.
        
9. **coach/ManagePlansController.java**: Controls ManagePlans.fxml.
    
    - **Functions**: Creates, updates, deletes plans for members via PlanUtil.
        
    - **Interacts With**: PlanUtil, MemberUtil, CoachUtil.
        
10. **coach/SendMessagesController.java**: Controls SendMessages.fxml.
    
    - **Functions**: Sends messages to members via MessageUtil.
        
    - **Interacts With**: MessageUtil, MemberUtil.
        
11. **member/MemberController.java**: Controls MemberDashboard.fxml.
    
    - **Functions**: Handles navigation to member views (e.g., ViewPlan.fxml).
        
    - **Interacts With**: ViewLoaderUtil.
        
12. **member/ViewMessagesController.java**: Controls ViewMessages.fxml.
    
    - **Functions**: Displays messages for the logged-in member.
        
    - **Interacts With**: MessageUtil.
        
13. **member/ViewNotificationsController.java**: Controls ViewNotifications.fxml.
    
    - **Functions**: Displays notifications for the logged-in member.
        
    - **Interacts With**: NotificationUtil.
        
14. **member/ViewPlanController.java**: Controls ViewPlan.fxml.
    
    - **Functions**: Displays the member’s assigned plan.
        
    - **Interacts With**: PlanUtil.
        
15. **member/ViewSubscriptionController.java**: Controls ViewSubscription.fxml.
    
    - **Functions**: Displays the member’s subscription details (e.g., expiryDate).
        
    - **Interacts With**: MemberUtil.
        
16. **ProfileController.java**: Controls Profile.fxml.
    
    - **Functions**: Allows users to view/edit profile details (e.g., username, password).
        
    - **Interacts With**: UserUtil, MemberUtil, CoachUtil.
        

## JSON Data Files

JSON files in src/main/resources/data/ store persistent data, managed by corresponding *Util classes.

1. **billing.json**: Stores billing records (Billing objects).
    
2. **coaches.json**: Stores coach data (Coach objects).
    
3. **members.json**: Stores member data (Member objects).
    
4. **messages.json**: Stores message data (Message objects).
    
5. **notifications.json**: Stores notification data (Notification objects).
    
6. **plans.json**: Stores fitness plan data (Plan objects).
    
7. **users.json**: Stores user data (User objects, including admins with ADMIN role).
    

## Notes

- **Technology Stack**:
    
    - **JavaFX**: Used for the UI, with FXML files defining layouts and controllers handling logic.
        
    - **Jackson**: Handles JSON serialization/deserialization via JsonUtil.
        
    - **Logging**: LoggerUtil provides logging for debugging and auditing.
        
- **Authentication**:
    
    - User model supports all roles (ADMIN, COACH, MEMBER) with a single users.json file.
        
    - AuthController validates credentials and directs to role-specific dashboards.
        
- **Search Functionality**:
    
    - Implemented in ManageMembersController.java and ManageCoachesController.java
        
    - Filters cards by name using TextField (case-insensitive, partial matches).
        
- **Data Validation**:
    
    - MemberUtil and CoachUtil validate inputs (e.g., unique usernames, future expiry dates).
        
    - Dialogs in ManageMembersController.java ensure required fields (e.g., no password for editing)
        
- **Module Configuration**:
    
    - module-info.java
        
        ```java
        module com.healthclub {  
    requires javafx.controls;  
    requires javafx.fxml;  
    requires com.fasterxml.jackson.databind;  
    requires java.logging;  
  
    opens com.healthclub to javafx.fxml, com.fasterxml.jackson.databind;  
    opens com.healthclub.controller to javafx.fxml, com.fasterxml.jackson.databind;  
    opens com.healthclub.model to javafx.fxml, com.fasterxml.jackson.databind;  
    opens com.healthclub.controller.admin to com.fasterxml.jackson.databind, javafx.fxml;  
    opens com.healthclub.controller.coach to com.fasterxml.jackson.databind, javafx.fxml;  
    opens com.healthclub.controller.member to com.fasterxml.jackson.databind, javafx.fxml;  
  
    exports com.healthclub.model;  
    exports com.healthclub.controller.admin;  
    exports com.healthclub.controller.member;  
    exports com.healthclub.controller.coach;  
    exports com.healthclub;  
  
        ```
        
- **Main Application**:
    
    - Main.java initializes the JavaFX application, loads login.fxml, and sets up the primary stage.
        
- **Extensibility**:
    
    - Add AssignMembers.fxml for explicit coach-member assignments if needed.
        
    - Extend ReportUtil to store reports in a dedicated reports.json.
        
    - Add schedule management via PlanUtil or a new ScheduleUtil.
