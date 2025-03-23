package com.example.demo;

import com.example.projekt3okienka.ClassContainer;
import com.example.projekt3okienka.ClassEmployee;
import com.example.projekt3okienka.Employee;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {


    private final List<Employee> employees = new ArrayList<>();
    private Map<String, ClassEmployee> groups = new HashMap<>();

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        employees.add(employee);
        System.out.println("Dodano pracownika: " + employee);
        return employee;
    }

    @DeleteMapping("/{imie}/{nazwisko}")
    public String removeEmployee(@PathVariable String imie, @PathVariable String nazwisko) {
        Employee employeeToRemove = employees.stream().filter
                        (employee -> employee.getImie().equals(imie) && employee.getNazwisko().equals(nazwisko))
                .findAny().orElse(null);
        if (employeeToRemove != null) {
            employees.remove(employeeToRemove);
            System.out.println("Usunieto pracownika: " + employeeToRemove);
            return "Usunieto pracownika " + imie + " " + nazwisko;
        } else {
            return "Pracownik o tym imieniu i nazwisku nie znajduje sie w bazie";
        }
    }


        @GetMapping
        public List<Employee> getAllEmployees() {
            return employees;
        }
    }
