package com.test.models;

import com.test.Hibernate;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "groups")
public class ClassEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private List<Employee> employees = new ArrayList<>();

//    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
//    @JoinColumn(name = "group_id")
//    private double rating;

    @Column(name = "max_employees")
    private int maxEmployees;

    public ClassEmployee() {}

    public ClassEmployee(String groupName, int maxEmployees) throws Exception{
        if(maxEmployees <= 0) throw new Exception("Rozmiar grupy musi być większy niż 0");

        this.groupName = groupName;
        this.maxEmployees = maxEmployees;
    }

    public Long getId(){
        return this.id;
    }

    public String getGroupName(){
        return this.groupName;
    }

    public int getCurrentSize(){
        return this.employees.size();
    }

    public int getMaxEmployees(){
        return this.maxEmployees;
    }

    public List<Employee> getEmployees(){
        return this.employees;
    }

    public Employee getEmployee(String fullName){
        return this.employees.stream().filter(e -> fullName.equals(e.getName() + " " + e.getSurname())).findFirst().orElse(null);
    }

    public void addEmployee(Employee employee) throws Exception{
        if(this.employees.size() == this.maxEmployees) throw new Exception("Grupa nie moze mieć więcej pracowników");

        if(this.employees.stream().anyMatch(employee1 -> Objects.equals(employee1.getName(), employee.getName()) &&
                Objects.equals(employee1.getSurname(), employee.getSurname()))) throw new Exception("Taki pracownik juz istnieje");

        employees.add(employee);
    }

    public void addSalary(Employee employee, double raise){
        employee.setSalary(employee.getSalary() + raise);
    }

    public void removeEmployee(Employee employee){
        this.employees = employees.stream().filter(employee1 ->
                !Objects.equals(employee1.getSurname(), employee.getSurname()) &&
                        !Objects.equals(employee1.getName(), employee.getName())).collect(Collectors.toCollection(ArrayList::new));
    }

    public void changeCondition(Employee employee, EmployeeCondition condition) throws Exception{
        if(condition == null) throw new Exception("Coś źle zrobiłeś");

        int index = this.employees.indexOf(employee);

        if(index == -1) throw new Exception("Nie ma takiego pracownika w grupie");
        this.employees.get(index).setCondition(condition);
    }

    public Employee search(String surname) throws Exception{
        Comparator<Employee> compareBySurname = new Comparator<Employee>() {
            @Override
            public int compare(Employee employee1, Employee employee2) {
                return employee1.getSurname().compareTo(employee2.getSurname());
            }
        };

        Optional<Employee> result = this.employees.stream()
                .filter(employee -> compareBySurname.compare(employee,
                        new Employee("", surname, EmployeeCondition.PRESENT, 0, 0)) == 0)
                .findFirst();

        if(result.isPresent()){
            return result.get();
        } else{
            throw new Exception("Nie ma pracownika o takim nazwisku");
        }
    }

    public List<Employee> searchPartial(String surname){
        return this.employees.stream().filter(employee -> employee.getSurname().contains(surname)).toList();
    }

    public long countByCondition(EmployeeCondition condition){
        Session session = Hibernate.getSessionFactory().openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);

        Predicate conditionPredicate = criteriaBuilder.equal(root.get("employeeCondition"), condition);
//        Predicate groupPredicate = criteriaBuilder.equal(classEmployeeJoin.get("id"), this.id);

//        criteriaQuery.select(criteriaBuilder.count(root)).where(criteriaBuilder.and(conditionPredicate, groupPredicate));

        Long count = session.createQuery(criteriaQuery).getSingleResult();

//        return employees.stream().filter(employee -> employee.getCondition() == condition).toList().size();
        return count;
    }

//    public void summary(){
//        for(Employee employee : this.employees){
//            employee.printing();
//        }
//    }

    public List<Employee> sortBy(String criteria){
        Session session = Hibernate.getSessionFactory().openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("groupId"), this.id));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(criteria)));
        List<Employee> employees = session.createQuery(criteriaQuery).getResultList();
        session.close();

        return employees;
    }

    public Employee max(){
        return Collections.max(this.employees);
    }
}
