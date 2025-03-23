module test {
    requires jakarta.persistence;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens com.test.models to org.hibernate.orm.core;
    opens com.test.controllers to javafx.fxml;

    exports com.test;
    exports com.test.models;
}