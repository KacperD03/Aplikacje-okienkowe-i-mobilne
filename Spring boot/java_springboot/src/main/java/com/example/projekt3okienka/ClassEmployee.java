package com.example.projekt3okienka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public class ClassEmployee {
    private int maxCapacity;
    private List<Employee> lista_pracownikow = new ArrayList<>();
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    public ClassEmployee(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void addEmployee(Employee employee) {
        if (lista_pracownikow.size() >= maxCapacity) {
            System.out.println("Nie można dodać więcej pracowników. Grupa jest pełna.");
            return;
        }

        for (Employee emp : lista_pracownikow) {
            if (emp.getImie().equalsIgnoreCase(employee.getImie()) &&
                    emp.getNazwisko().equalsIgnoreCase(employee.getNazwisko())) {
                System.out.println("Pracownik o tym imieniu i nazwisku już istnieje.");
                return;
            }
        }

        lista_pracownikow.add(employee);
        employeeList.add(employee);
    }

    public void removeEmployeeFromJob(String imie, String nazwisko) {
        lista_pracownikow.removeIf(emp -> emp.getImie().equalsIgnoreCase(imie) &&
                emp.getNazwisko().equalsIgnoreCase(nazwisko));
        employeeList.setAll(lista_pracownikow);
    }

    public ObservableList<Employee> getEmployeeList() {
        return FXCollections.unmodifiableObservableList(employeeList);
    }

    public boolean isEmpty() {
        return lista_pracownikow.isEmpty();
    }

    public double getFillPercentage() {
        return (double) lista_pracownikow.size() / maxCapacity * 100;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentSize() {
        return lista_pracownikow.size();
    }
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(lista_pracownikow);
    }
}
