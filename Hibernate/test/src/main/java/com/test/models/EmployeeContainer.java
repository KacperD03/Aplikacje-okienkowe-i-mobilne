package com.test.models;

import java.util.*;

public class EmployeeContainer {
    private static EmployeeContainer instance = null;
    private final List<Employee> employees = new ArrayList<>();

    private EmployeeContainer(){}

    public static EmployeeContainer getInstance(){
        if(instance == null){
            instance = new EmployeeContainer();
        }

        return instance;
    }

    public void addEmployee(Employee e){
        this.employees.add(e);
    }

    public List<Employee> getEmployees(){
        return this.employees;
    }

    public Employee getEmployee(String fullName){
        for(Employee e : this.employees){
            if(Objects.equals(fullName, e.getName() + " " + e.getSurname())) return e;
        }
        throw new IllegalArgumentException("Błędne dane ziomeczka");
    }

    public void removeEmployee(Employee e){
        this.employees.remove(e);
    }
}
