module com.lms.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires spring.web;
    requires static lombok;

    opens com.lms.frontend to javafx.fxml;
    exports com.lms.frontend;
}