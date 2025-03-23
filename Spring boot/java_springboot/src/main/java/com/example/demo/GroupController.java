package com.example.demo;

import com.example.projekt3okienka.ClassContainer;
import com.example.projekt3okienka.ClassEmployee;
import com.example.projekt3okienka.Employee;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private ClassContainer classContainer;

    public GroupController() {
        this.classContainer = new ClassContainer();
        this.classContainer.addClass("Grupa1", 10);
        this.classContainer.addClass("Grupa2", 5);
        this.classContainer.addClass("Grupa3", 4);
    }

    @GetMapping
    public List<String> getAllGroupNames() {
        return classContainer.getAllGroupNames();


    }

    @PostMapping
    public String addGroup(@RequestBody Map<String, Object> requestData) {
        String groupName = (String) requestData.get("name");
        int capacity = (int) requestData.get("capacity");

        classContainer.addClass(groupName, capacity);
        return "Dodano grupę: " + groupName + " z pojemnością: " + capacity;
    }

    @DeleteMapping("/{name}")
    public String removeGroup(@PathVariable String name) {
        if (classContainer.getAllGroupNames().contains(name)) {
            classContainer.removeClass(name);
            return "Usunieto grupe " + name;
        } else {
            return "Grupa o nazwie:" + name + " nie istnieje";
        }
    }

       @PostMapping("/{groupName}/employee")
        public String addEmployeeToGroup(@PathVariable String groupName, @RequestBody Employee employee) {
            ClassEmployee group = classContainer.getGroup(groupName);
            if (group != null) {
                if (employee != null) {
                    group.addEmployee(employee);
                    System.out.println("Dodano pracownika " + employee + " do grupy: " + groupName);
                    return "Dodano pracownika " + employee + " do grupy: " + groupName;
                } else {
                    System.out.println("Nie można dodać pustego pracownika.");
                    return "Nie można dodać pustego pracownika.";
                }
            } else {
                System.out.println("Grupa o nazwie: " + groupName + " nie istnieje");
                return "Grupa o nazwie: " + groupName + " nie istnieje";
            }
        }


    @GetMapping("/{groupName}/employee")
    public List<Employee> getEmployeesInGroup(@PathVariable String groupName) {
        ClassEmployee group = classContainer.getGroup(groupName);
        if (group != null) {
            System.out.println("Pobrano listę pracowników z grupy: " + groupName);
            return group.getAllEmployees();
        } else {
            System.out.println("Grupa o nazwie: " + groupName + " nie istnieje");
            return new ArrayList<>();
        }
    }
}