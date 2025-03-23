package com.example.projekt3okienka;

import java.util.*;

public class ClassContainer {
    private Map<String, ClassEmployee> groups = new HashMap<>();

    public void addClass(String groupName, int capacity) {
        if (!groups.containsKey(groupName)) {
            groups.put(groupName, new ClassEmployee(capacity));
            System.out.println("Dodano grupę: " + groupName + " z pojemnością: " + capacity);
        } else {
            System.out.println("Grupa " + groupName + " już istnieje.");
        }
    }

    public void removeClass(String groupName) {
        groups.remove(groupName);
        System.out.println("Usunięto grupę: " + groupName);
    }

    public void addEmployeeToGroup(String groupName, Employee employee) {
        ClassEmployee group = groups.get(groupName);
        if (group != null) {
            if (group.getCurrentSize() >= group.getMaxCapacity()) {
                System.out.println("Nie można dodać pracownika. Grupa " + groupName + " jest pełna.");
            } else {
                group.addEmployee(employee);
                System.out.println("Dodano pracownika do grupy: " + groupName);
            }
        } else {
            System.out.println("Grupa " + groupName + " nie istnieje.");
        }
    }

    public void removeEmployeeFromGroup(String groupName, Employee employee) {
        ClassEmployee group = groups.get(groupName);
        if (group != null) {
            group.removeEmployeeFromJob(employee.getImie(), employee.getNazwisko());
            System.out.println("Usunięto pracownika z grupy: " + groupName);
        } else {
            System.out.println("Grupa " + groupName + " nie istnieje.");
        }
    }
    public void removeEmployeeFromAllGroups(Employee employee) {
        for (ClassEmployee group : groups.values()) {
            group.removeEmployeeFromJob(employee.getImie(), employee.getNazwisko());
        }
        System.out.println("Usunięto pracownika ze wszystkich grup.");
    }


    public List<String> findEmpty() {
        List<String> emptyGroups = new ArrayList<>();
        for (Map.Entry<String, ClassEmployee> entry : groups.entrySet()) {
            if (entry.getValue().isEmpty()) {
                emptyGroups.add(entry.getKey());
            }
        }
        return emptyGroups;
    }

    public void summary() {
        for (Map.Entry<String, ClassEmployee> entry : groups.entrySet()) {
            String groupName = entry.getKey();
            ClassEmployee group = entry.getValue();
            double fillPercentage = group.getFillPercentage();
            System.out.printf("Grupa: %s, Wypełniona: %.2f%%\n", groupName, fillPercentage);
        }
    }

    public ClassEmployee getGroup(String groupName) {
        return groups.get(groupName);
    }
    public List<String> getAllGroupNames() {
        return new ArrayList<>(groups.keySet());
    }

    public List<ClassEmployee> getAllGroups() {
        return new ArrayList<>(groups.values());
    }

    public Map<String, ClassEmployee> getAllGroupsWithNames() {
        return new HashMap<>(groups);
    }
}
