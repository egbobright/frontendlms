module com.lms.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;



    exports com.lms.frontend;

    //opens com.lms.frontend.models to javafx.base;
    opens com.lms.frontend.models to javafx.base, javafx.graphics, com.fasterxml.jackson.databind;
    //opens com.lms.frontend.models to com.fasterxml.jackson.databind;
    opens com.lms.frontend to javafx.graphics;
    opens com.lms.frontend.service to javafx.base;
    opens com.lms.frontend.views to javafx.fxml;
}