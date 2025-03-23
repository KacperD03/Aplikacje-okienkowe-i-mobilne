package com.test.controllers;

import com.test.Hibernate;
import com.test.models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShowGroupsController {

    @FXML
    private VBox groupsContainer;

    @FXML
    private VBox groupDetailsContainer;

    @FXML
    private Text noGroupsText;

    @FXML
    private VBox commonBox;

    @FXML
    private ScrollPane leftScroll;

    @FXML
    private ScrollPane rightScroll;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchBut;

    @FXML
    private Button searchPartialBut;

    @FXML
    private ToggleGroup sortTgGroup = new ToggleGroup();

    @FXML
    private ComboBox<EmployeeCondition> countCond;

    @FXML
    private Button countCondBut;

    @FXML
    private Text countCondTxt;

    @FXML
    private ComboBox<Employee> addEmp;

    @FXML
    private Button addEmpBut;


    @FXML
    private void initialize() {
        try {
            createRadioButton(sortTgGroup, "Sortuj według nazwiska", true);
            createRadioButton(sortTgGroup, "Sortuj według pensji", false);

            countCond.setItems(FXCollections.observableArrayList(EmployeeCondition.values()));

            commonBox.getChildren().addAll(new Separator());
            displayGroups();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void displayGroups() {
        groupsContainer.getChildren().clear();
        groupDetailsContainer.setVisible(false);
        groupDetailsContainer.setPadding(new Insets(5, 5, 5, 5));
        commonBox.setVisible(false);
        leftScroll.setVisible(false);
        rightScroll.setVisible(false);

        Session session = Hibernate.getSessionFactory().openSession();
        List<ClassEmployee> groups = session.createQuery("from ClassEmployee", ClassEmployee.class).stream().collect(Collectors.toCollection(ArrayList::new));

        if(groups.isEmpty()){
            noGroupsText.setText("Brak grup");
            return;
        } else{
            leftScroll.setVisible(true);
        }

        for (ClassEmployee entry : groups){
            final ClassEmployee classEmployee = entry;

            final HBox groupBox = new HBox();

            VBox vb1 = new VBox();
            VBox vb2 = new VBox();

            Label nameLabel = new Label("Nazwa grupy: " + classEmployee.getGroupName());
            Label sizeLabel = new Label("Obecny Rozmiar: " + classEmployee.getCurrentSize());
            Label maxEmployeesLabel = new Label("Max. rozmiar: " + classEmployee.getMaxEmployees());

            Button deleteButton = new Button("Usuń");
            deleteButton.setOnAction(_ -> deleteGroup(classEmployee.getId()));

            nameLabel.setFont(new Font("Arial", 14));
            sizeLabel.setFont(new Font("Arial", 14));
            maxEmployeesLabel.setFont(new Font("Arial", 14));
            deleteButton.setFont(new Font("Arial", 14));

            vb1.getChildren().addAll(nameLabel, sizeLabel, maxEmployeesLabel);

            vb2.getChildren().add(deleteButton);
            vb2.setPadding(new Insets(15, 0, 10, 50));

            groupBox.getChildren().addAll(vb1, vb2);
            groupBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            groupBox.setPadding(new Insets(5));
            groupBox.setOnMouseClicked(_ -> {
                for(Toggle tg : sortTgGroup.getToggles()){
                    if(tg instanceof RadioButton rb){
                        rb.setOnAction(_ -> {
                            if(Objects.equals(rb.getText(), "Sortuj według nazwiska")) {
                                showGroupDetails(classEmployee, classEmployee.sortBy("surname"));
                            }
                            else {
                                showGroupDetails(classEmployee, classEmployee.sortBy("salary"));
                            }
                        });
                    }
                }

                countCondBut.setOnAction(_ -> {
                    try {
                        if (countCond.getValue() == null) throw new Exception("Stan jest pusty");
                        countCondTxt.setText(countCond.getValue() + ": " + (int) classEmployee.countByCondition(countCond.getValue()));
                    } catch (Exception ex){
                        Utils.showAlert("Błąd", ex.getMessage(), Alert.AlertType.ERROR);
                    }
                });

                List<Employee> employees = new ArrayList<>(session.createQuery("from Employee", Employee.class).stream().toList());

                addEmp.setItems(FXCollections.observableArrayList(employees));
                addEmp.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(Employee e) {
                        return e != null ? e.getName() + " " + e.getSurname() : "";
                    }

                    @Override
                    public Employee fromString(String s) {
                        return null;
                    }

                });

                addEmpBut.setOnAction(_ -> {
                    try {
                        classEmployee.addEmployee(addEmp.getValue());
                        addEmp.getValue().setGroupId(classEmployee.getId());
                        Transaction transaction = session.beginTransaction();
                        session.merge(classEmployee);
                        transaction.commit();
                        displayGroups();
                        showGroupDetails(classEmployee, classEmployee.getEmployees());
                        commonBox.setVisible(true);
                        addEmp.setValue(null);
                    } catch (Exception ex) {
                        Utils.showAlert("Błąd", ex.getMessage(), Alert.AlertType.ERROR);
                    }
                });

                commonBox.setVisible(true);

                showGroupDetails(classEmployee, this.getSortedEmployeesList(classEmployee));

            });

            groupsContainer.getChildren().add(groupBox);
        }
    }

    @FXML
    private void showGroupDetails(ClassEmployee classEmployee, List<Employee> employees){
        groupDetailsContainer.setVisible(true);
        groupDetailsContainer.getChildren().clear();

        searchPartialBut.setOnAction(_ -> {
            try {
                filterPartial(classEmployee);
            } catch (Exception ex) {
                Utils.showAlert("Błąd", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        searchBut.setOnAction(_ -> {
            try {
                filter(classEmployee);
            } catch (Exception ex) {
                Utils.showAlert("Błąd", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        if(!employees.isEmpty()) rightScroll.setVisible(true);

        VBox vb = createEmployeesList(employees, classEmployee);
        vb.setSpacing(10);

        groupDetailsContainer.getChildren().add(vb);
    }

    @FXML
    private VBox createEmployeesList(List<Employee> empList, ClassEmployee empClass){
        VBox vb = new VBox();

        for(Employee e : empList){
            final Employee employee = e;
            Text empText = new Text(e.getName() + " " + e.getSurname() +
                    ", " + e.getCondition() + ", " + e.getBirthYear() + ", " + e.getSalary());
            empText.setFont(new Font("Arial", 14));

            Button delBut = getButton("Usuń pracownika z listy", empClass,
                    () -> {
                        empClass.removeEmployee(e);
                        Session session = Hibernate.getSessionFactory().openSession();
                        Transaction transaction = session.beginTransaction();
                        session.merge(empClass);
                        transaction.commit();
                        session.close();
                        showGroupDetails(empClass, this.getSortedEmployeesList(empClass));
                    });
            Button delPermBut = getButton("Usuń pracownika całkowicie",
                    empClass, () -> {
                        empClass.removeEmployee(e);
                        Session session = Hibernate.getSessionFactory().openSession();
                        Transaction transaction = session.beginTransaction();
                        session.remove(e);
                        transaction.commit();

                        addEmp.setItems(FXCollections.observableList(session.get(ClassEmployee.class, empClass.getId()).getEmployees()));

                        session.close();
                        showGroupDetails(empClass, this.getSortedEmployeesList(empClass));
                    });
            Button editBut = getButton("Edytuj pracownika",
                    empClass, () -> {
                        try {
                            WindowBuilder builder = new WindowBuilder();
                            builder.setViewPath("/com/test/views/EmployeeView.fxml").
                                    setTitle("Edytuj pracownika").setWidth(640).setHeight(480).setControllerData(e);
                            Stage st = builder.build();
                            st.setOnCloseRequest(_ -> {
                                addEmp.setItems(FXCollections.observableArrayList(empClass.getEmployees()));

                                showGroupDetails(empClass, this.getSortedEmployeesList(empClass));
                                commonBox.setVisible(true);
                            });
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });

            HBox hb = new HBox();
            hb.setSpacing(10);
            final TextField addSal = new TextField();
            addSal.setPrefWidth(80);
            Button addSalBut = getAddSalBut(empClass, employee, addSal);
            hb.getChildren().addAll(addSal, addSalBut);

            HBox hb2 = new HBox();
            hb2.setSpacing(10);
            final ComboBox<EmployeeCondition> empCond = new ComboBox<>();
            empCond.setItems(FXCollections.observableArrayList(EmployeeCondition.values()));
            empCond.setValue(e.getCondition());
            Button chCondBut = getChCondBut(empClass, employee, empCond);
            hb2.getChildren().addAll(empCond, chCondBut);

            vb.getChildren().addAll(empText, delBut, delPermBut, editBut, new Separator(), hb, new Separator(), hb2);

            Separator elemSep = new Separator();
            if(empList.indexOf(e) != empList.size() - 1) vb.getChildren().add(elemSep);
        }

        return vb;
    }

    @FXML
    private Button getButton(String butText, ClassEmployee empClass, Runnable function) {
        Button but = new Button(butText);
        but.setOnAction(_ -> {
            function.run();
            displayGroups();
            showGroupDetails(empClass, empClass.getEmployees());
            commonBox.setVisible(true);
        });
        return but;
    }

    @FXML
    private Button getAddSalBut(ClassEmployee classEmployee, Employee employee, TextField addSalField) {
        Button addSalBut = new Button("Dodaj kwotę do wynagrodzenia");
        addSalBut.setOnAction(_ -> {
            try {
                Session session = Hibernate.getSessionFactory().openSession();
                Transaction transaction = session.beginTransaction();
                classEmployee.addSalary(employee, Double.parseDouble(addSalField.getText()));
                session.merge(classEmployee);
                transaction.commit();
                session.close();
                showGroupDetails(classEmployee, this.getSortedEmployeesList(classEmployee));
            } catch (NumberFormatException exception){
                Utils.showAlert("Błąd", "Niepoprawna kwota", Alert.AlertType.ERROR);
            } catch (Exception exception){
                Utils.showAlert("Błąd", exception.getMessage(), Alert.AlertType.ERROR);
            }
        });
        return addSalBut;
    }

    @FXML
    private Button getChCondBut(ClassEmployee classEmployee, Employee employee, ComboBox<EmployeeCondition> cond) {
        Button chCond = new Button("Zmień stan pracownika");
        chCond.setOnAction(_ -> {
            try {
                Session session = Hibernate.getSessionFactory().openSession();
                Transaction transaction = session.beginTransaction();
                classEmployee.changeCondition(employee, cond.getValue());
                session.merge(classEmployee);
                transaction.commit();
                session.close();
                showGroupDetails(classEmployee, this.getSortedEmployeesList(classEmployee));
            } catch (Exception ex) {
                Utils.showAlert("Błąd", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
        return chCond;
    }

    @FXML
    private void createRadioButton(ToggleGroup tgGroup, String text, boolean selected){
        RadioButton rb = new RadioButton(text);

        rb.setToggleGroup(tgGroup);
        rb.setSelected(selected);

        commonBox.getChildren().add(rb);
    }

    @FXML
    private void filter(ClassEmployee classEmployee) throws Exception {
        if(Objects.equals(searchTextField.getText(), "")) throw new Exception("Puste pole tekstowe");
        Employee emp = classEmployee.search(searchTextField.getText());
        List<Employee> empList = new ArrayList<>();
        empList.add(emp);
        showGroupDetails(classEmployee, empList);
    }

    @FXML
    private void filterPartial(ClassEmployee classEmployee) throws Exception {
        if(Objects.equals(searchTextField.getText(), "")) throw new Exception("Puste pole tekstowe");
        showGroupDetails(classEmployee, classEmployee.searchPartial(searchTextField.getText()));
    }

    private void deleteGroup(Long id){
        Session session = Hibernate.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        ClassEmployee group = session.get(ClassEmployee.class, id);
        if(group != null){
            session.remove(group);
        }
        transaction.commit();
        session.close();
        displayGroups();
    }

    private List<Employee> getSortedEmployeesList(ClassEmployee classEmployee){
        return Objects.equals(((RadioButton) sortTgGroup.getSelectedToggle()).getText(), "Sortuj według nazwiska") ?
                classEmployee.sortBy("surname") : classEmployee.sortBy("salary");
    }
}
