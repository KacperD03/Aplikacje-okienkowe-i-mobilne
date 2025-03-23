package com.test.models;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee implements Comparable<Employee> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "employee_condition")
    @Enumerated(EnumType.STRING)
    private EmployeeCondition employeeCondition;

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "salary")
    private double salary;

    @Column(name = "group_id")
    private Long groupId;

    public Employee() {}

    public Employee(String name, String surname,
                    EmployeeCondition employeeCondition, int birthYear, double salary)
    {
        this.name = name;
        this.surname = surname;
        this.employeeCondition = employeeCondition;
        this.birthYear = birthYear;
        this.salary = salary;
    }

    public Long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return this.surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public EmployeeCondition getCondition(){
        return this.employeeCondition;
    }

    public void setCondition(EmployeeCondition condition){
        this.employeeCondition = condition;
    }

    public double getSalary(){
        return this.salary;
    }

    public void setSalary(double newSalary){
        this.salary = newSalary;
    }

    public int getBirthYear(){
        return this.birthYear;
    }

    public void setBirthYear(int birthYear){
        this.birthYear = birthYear;
    }

    public void setGroupId(Long id){
        this.groupId = id;
    }

    @Override
    public int compareTo(Employee o) {
        return this.surname.compareTo(o.getSurname());
    }
}
