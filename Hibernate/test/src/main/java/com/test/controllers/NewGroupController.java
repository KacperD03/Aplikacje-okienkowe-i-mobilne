package com.test.controllers;

import com.test.Hibernate;
import com.test.models.ClassEmployee;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.test.models.ClassContainer;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NewGroupController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField sizeField;

    @FXML
    private Button saveButton;

    @FXML
    private void initialize(){
        saveButton.setOnAction(e -> handleSaveEmployee());
    }

    @FXML
    private void handleSaveEmployee() {
        try {
            String name = nameField.getText();
            int size = Integer.parseInt(sizeField.getText());

            if(name.isEmpty()) {
                Utils.showAlert("Błąd", "Nazwa grupy nie może być pusta", Alert.AlertType.ERROR);
                return;
            }

            Session session = Hibernate.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();

            ClassEmployee newCE = new ClassEmployee(name, size);

            session.persist(newCE);
            transaction.commit();
//            Hibernate.shutdown();

            Utils.showAlert("Błąd", "Dodano grupę", Alert.AlertType.CONFIRMATION);
            clearFields();
        } catch (NumberFormatException e){
            Utils.showAlert("Błąd", "Niepoprawny rozmiar grupy", Alert.AlertType.ERROR);
        } catch (Exception e) {
            Utils.showAlert("Błąd", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clearFields(){
        nameField.clear();
        sizeField.clear();
    }
}
