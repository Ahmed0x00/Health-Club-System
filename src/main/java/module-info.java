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

}