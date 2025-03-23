module com.example.projekt3okienka {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    //requires spring.data.jpa;
    requires spring.core;
    requires spring.beans;
    //requires spring.jdbc;

  //  requires java.sql;
   // requires java.persistence;
    //requires mysql.connector.j;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    requires java.desktop;
    requires org.apache.tomcat.embed.core;

    exports com.example.projekt3okienka;
    exports com.example.demo;
    //exports com.example.Controllers;

    opens com.example.projekt3okienka to
            javafx.fxml,
            javafx.graphics,
            spring.core,
            spring.beans,
            spring.boot,
            com.fasterxml.jackson.databind;

    opens com.example.demo to
            javafx.fxml,
            javafx.graphics,
            spring.core,
            spring.beans,
            spring.boot,
            com.fasterxml.jackson.databind;
  /*  opens com.example.Controllers to
            javafx.fxml,
            javafx.graphics,
            spring.core,
            spring.beans,
            spring.boot,
            com.fasterxml.jackson.databind; // Dodano otwarcie dla Jacksona */
}