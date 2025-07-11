package com.example.demoinitial.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity(name = "Employee")
@NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.name = :name")
// @EntityListeners({LogListener.class})
public class Employee extends BaseEntity {

    private String name;
    private long salary;

    @OneToMany(mappedBy = "chef", orphanRemoval = true, cascade = CascadeType.ALL )
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Employee> employees = new HashSet<>();

    @ManyToOne
    private Employee chef;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL )
    private Set<Phone> phones = new HashSet<Phone>();
    @ManyToOne
    private Department department;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

    @ManyToMany(mappedBy = "employees")
    private Set<Project> projects = new HashSet<Project>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" id: ");
        sb.append(this.id);
        sb.append(" name: ");
        sb.append(this.name);
        sb.append(" salary: ");
        sb.append(this.salary);
        return sb.toString();
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Employee getChef() {
        return chef;
    }

    public void setChef(Employee chef) {
        this.chef = chef;
    }
}
