package com.example.projekt3okienka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class HelloController {
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> colImie;
    @FXML private TableColumn<Employee, String> colNazwisko;
    @FXML private TableColumn<Employee, Double> colWynagrodzenie;
    @FXML private TableColumn<Employee, String> colStan;
    @FXML private TableColumn<Employee, Integer> colRokUrodzenia;
    @FXML private ListView<String> groupListView;
    @FXML private TextField groupNameField;
    @FXML private TextField capacityField;

    @FXML private TextField textFilter;
    @FXML private TextField imieField;
    @FXML private TextField nazwiskoField;
    @FXML private TextField wynagrodzenieField;
    @FXML private TextField rok_urodzeniaField;
    @FXML private ComboBox<String> stanComboBox;

    private ClassContainer classContainer = new ClassContainer();
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private ObservableList<Employee> displayedEmployees = FXCollections.observableArrayList();
    private String currentGroup = null;

    @FXML
    public void initialize() {
        // Inicjalizacja kolumn
        displayedEmployees.setAll(employeeList);
        colImie.setCellValueFactory(cellData -> cellData.getValue().imieProperty());
        colNazwisko.setCellValueFactory(cellData -> cellData.getValue().nazwiskoProperty());
        colWynagrodzenie.setCellValueFactory(cellData -> cellData.getValue().wynagrodzenieProperty().asObject());
        colStan.setCellValueFactory(cellData -> cellData.getValue().stanPracownikaProperty().asString());
        colRokUrodzenia.setCellValueFactory(cellData -> cellData.getValue().rokUrodzeniaProperty().asObject());

        // Inicjalizacja tabeli
        employeeTable.setItems(displayedEmployees);
        employeeTable.setItems(displayedEmployees);

        // Inicjalizacja ComboBoxa
        stanComboBox.setItems(FXCollections.observableArrayList("OBECNY", "DELEGACJA", "CHORY", "NIEOBECNY"));
        stanComboBox.setValue("OBECNY");

        // Listener na filtr tekstowy
        textFilter.setOnKeyReleased(this::filterEmployees);
    }

    @FXML
    private void filterEmployees(KeyEvent event) {
        String filterText = textFilter.getText().toLowerCase();
        if (filterText.isEmpty()) {
            displayedEmployees.setAll(employeeList);
        } else {
            displayedEmployees.setAll(employeeList.filtered(employee ->
                    employee.getNazwisko().toLowerCase().contains(filterText)));
        }
        employeeTable.refresh();
    }

    @FXML
    public void addEmployee(ActionEvent event) {
        try {
            String imie = imieField.getText();
            String nazwisko = nazwiskoField.getText();
            double wynagrodzenie = Double.parseDouble(wynagrodzenieField.getText());
            int rokUrodzenia = Integer.parseInt(rok_urodzeniaField.getText());
            String stan = stanComboBox.getValue();

            if (imie.isEmpty() || nazwisko.isEmpty()) {
                showAlert("Błąd", "Imię i nazwisko nie mogą być puste.");
                return;
            }

            Employee newEmployee = new Employee(imie, nazwisko, wynagrodzenie, stan, rokUrodzenia);
            employeeList.add(newEmployee);
            employeeTable.refresh();
            clearEmployeeFields();
        } catch (NumberFormatException e) {
            showAlert("Błąd", "Nieprawidłowy format danych.");
        }
    }

    @FXML
    public void sortEmployeesByName(ActionEvent event) {
        displayedEmployees.sort((e1, e2) -> e1.getImie().compareToIgnoreCase(e2.getImie()));
        employeeTable.refresh();
    }

    @FXML
    public void sortEmployeesBySalary(ActionEvent event) {
        displayedEmployees.sort((e1, e2) -> Double.compare(e1.getWynagrodzenie(), e2.getWynagrodzenie()));
        employeeTable.refresh();
    }

    @FXML
    public void addGroup(ActionEvent event) {
        String groupName = groupNameField.getText();
        try {
            int capacity = Integer.parseInt(capacityField.getText());
            classContainer.addClass(groupName, capacity);
            groupListView.setItems(FXCollections.observableArrayList(classContainer.findEmpty()));
            groupNameField.clear();
            capacityField.clear();
        } catch (NumberFormatException e) {
            showAlert("Błąd", "Pojemność musi być liczbą całkowitą.");
        }
    }

    @FXML
    public void removeGroup(ActionEvent event) {
        String selectedGroup = groupListView.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            classContainer.removeClass(selectedGroup);
            groupListView.setItems(FXCollections.observableArrayList(classContainer.findEmpty()));
        }
    }

    @FXML
    public void showGroupEmployees(ActionEvent event) {
        String selectedGroup = groupListView.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
            ClassEmployee group = classContainer.getGroup(selectedGroup);
            if (group != null) {
                displayedEmployees.setAll(group.getEmployeeList());
                currentGroup = selectedGroup; // Ustawiamy aktualnie wybraną grupę
            } else {
                showAlert("Błąd", "Nie znaleziono grupy.");
            }
        } else {
            showAlert("Błąd", "Nie wybrano grupy.");
        }
    }

    @FXML
    public void showAllEmployees(ActionEvent event) {
        displayedEmployees.setAll(employeeList);
        currentGroup = null; // Brak wybranej grupy
    }

    @FXML
    public void refreshEmployeeList(ActionEvent event) {
        employeeTable.refresh();
    }

    @FXML
    public void removeEmployee(ActionEvent event) {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            // Usuń pracownika z głównej listy
            employeeList.remove(selectedEmployee);
            displayedEmployees.setAll(employeeList);

            // Usuń pracownika ze wszystkich grup
            classContainer.removeEmployeeFromAllGroups(selectedEmployee);

            employeeTable.refresh();
            showAlert("Sukces", "Pracownik został usunięty.");
        } else {
            showAlert("Błąd", "Nie wybrano pracownika.");
        }
    }

    @FXML
    public void addEmployeeToGroup(ActionEvent event) {
        String selectedGroup = groupListView.getSelectionModel().getSelectedItem();
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedGroup == null) {
            showAlert("Błąd", "Nie wybrano grupy.");
            return;
        }

        if (selectedEmployee == null) {
            showAlert("Błąd", "Nie wybrano pracownika.");
            return;
        }

        ClassEmployee group = classContainer.getGroup(selectedGroup);
        if (group.getCurrentSize() >= group.getMaxCapacity()) {
            showAlert("Błąd", "Grupa jest pełna. Nie można dodać kolejnego pracownika.");
            return;
        }

        classContainer.addEmployeeToGroup(selectedGroup, selectedEmployee);
        displayedEmployees.setAll(group.getEmployeeList()); // Aktualizacja wyświetlanej listy
        employeeTable.refresh();
        showAlert("Sukces", "Dodano pracownika do grupy " + selectedGroup);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearEmployeeFields() {
        imieField.clear();
        nazwiskoField.clear();
        wynagrodzenieField.clear();
        rok_urodzeniaField.clear();
        stanComboBox.setValue("OBECNY");
    }
}
