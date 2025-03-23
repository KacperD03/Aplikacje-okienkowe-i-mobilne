package com.example.demo;
import com.example.projekt3okienka.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GenerateCSV {
    @GetMapping("/employee/export")
    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        String fileName = "employee.csv";

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;

        response.setHeader(headerKey, headerValue);

        List<Employee> employees = getAllEmployees();

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Imie,Nazwisko,Wynagrodzenie,Rok Urodzenia,Stan Pracownika");

            for (Employee employee : employees) {
                writer.println(employee.getImie() + "," +
                        employee.getNazwisko() + "," +
                        employee.getWynagrodzenie() + "," +
                        employee.getRokUrodzenia() + "," +
                        employee.getStanPracownika());
            }


        }
    }
public List<Employee> getAllEmployees(){
      List<Employee> employees = new ArrayList<>();
employees.add(new Employee("Marcin","Krol",5000,"OBECNY",2004));
    employees.add(new Employee("Kacper","Kowalski",3000,"NIEOBECNY",2003));
return employees;
}



}
