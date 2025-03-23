package com.test.controllers;

import com.test.Hibernate;
import com.test.models.EmployeeContainer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.test.models.EmployeeCondition;
import com.test.models.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EmployeeController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private ComboBox<EmployeeCondition> employeeConditionField;

    @FXML
    private TextField birthYearField;

    @FXML
    private TextField salaryField;

    @FXML
    private Button saveButton;

    private Employee editEmp = null;

    @FXML
    private void initialize(){
        saveButton.setOnAction(e -> handleSaveEmployee());
        employeeConditionField.setItems(FXCollections.observableArrayList(EmployeeCondition.values()));
    }

    @FXML
    private void handleSaveEmployee() {
        try {
            String name = nameField.getText();
            String surname = surnameField.getText();
            EmployeeCondition condition = employeeConditionField.getValue();
            int birthYear = Integer.parseInt(birthYearField.getText());
            double salary = Double.parseDouble(salaryField.getText());

            if(name.isEmpty() || surname.isEmpty() || condition == null) {
                Utils.showAlert("Błąd", "Pole nie może być puste", Alert.AlertType.ERROR);
                return;
            }

            this.DBOperation(name, surname, condition, birthYear, salary);

            Utils.showAlert("Sukces", editEmp != null ? "Edytowano pracownika" : "Dodano pracownnika", Alert.AlertType.CONFIRMATION);
            clearFields();
        } catch (NumberFormatException e){
            Utils.showAlert("Błąd", "Niepoprawny rok urodzenia lub pensja", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void setData(Employee e){
        nameField.setText(e.getName());
        surnameField.setText(e.getSurname());
        employeeConditionField.setValue(e.getCondition());
        birthYearField.setText(Integer.toString(e.getBirthYear()));
        salaryField.setText(Double.toString(e.getSalary()));
        this.editEmp = e;
    }

    @FXML
    private void clearFields(){
        nameField.clear();
        surnameField.clear();
        birthYearField.clear();
        salaryField.clear();
        employeeConditionField.setValue(null);
    }

    private void DBOperation(String name, String surname, EmployeeCondition employeeCondition, int birthYear, double salary){
        try {
            Session session = Hibernate.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            if(editEmp != null){
                Employee tempEmp = session.get(Employee.class, editEmp.getId());
                if(tempEmp != null){
                    tempEmp.setName(name);
                    tempEmp.setSurname(surname);
                    tempEmp.setCondition(employeeCondition);
                    tempEmp.setBirthYear(birthYear);
                    tempEmp.setSalary(salary);
                    this.editEmp = tempEmp;
                }
            } else {
                Employee newEmp = new Employee(name, surname, employeeCondition, birthYear, salary);
                session.persist(newEmp);
            }
            transaction.commit();
            session.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
