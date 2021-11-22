package com.example.demoinitial.config;

import com.example.demoinitial.domain.*;
import com.example.demoinitial.repository.*;
import com.example.demoinitial.utils.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Defines a Bean for the dev-Profile
 */
@Configuration
@Profile("dev")
@Transactional
// @ConditionalOnClass(name = {"org.h2.Driver"})
public class DevConfiguration implements HasLogger {

    @Autowired
    @Qualifier("felixMuster")
    Person felixMuster;

    @Autowired
    @Qualifier("maxMustermann")
    Person maxMustermann;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ProjectRepository projectRepository;

    public DevConfiguration() {
        getLogger().info("Dev Configuration Class");

    }

    @PostConstruct
    public void createData() {
        createPersonData();
        Employee employeeFelixMuster = createEmployeeFelixMuster();
        Employee employeeJohnDoe = createEmployeeJohnDoe();

        createDepartment();

        createPhone(employeeFelixMuster.getId());

        createDesignProject();
        createQualityProject();

        createEmployeeMaxMustermann();
        assignMaxMustermannToDesignProject();
        assignMaxMustermannAsChef();

    }


    private void createPersonData() {
        personRepository.save(felixMuster);
        personRepository.save(maxMustermann);
        getLogger().debug("Person felixMuster and  maxMustermann saved to DB");

        List<Person> persons = personRepository.findQueryByLastName("Mustermann");
        persons.forEach(person -> getLogger().debug("findQueryByLastName Mustermann = " + person.toString()));

        personRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName")).forEach(person -> {
            personRepository.findById(person.getId())
                    .ifPresent(p -> getLogger().debug("findAll, Sort by lastName ASC = " + p.toString()));
        });
    }

    private Employee createEmployeeFelixMuster() {

        Address address = new Address();
        address.setCity("Zürich");
        address.setState("ZH");
        address.setStreet("Lagerstrasse 41");
        address.setZip("8004");

        Employee emp = new Employee();
        emp.setAddress(address);
        emp.setName("Felix Muster");
        emp.setSalary(80000);
        emp = employeeRepository.save(emp);

        return emp;
    }

    private Employee createEmployeeJohnDoe() {
        Address address = new Address();
        address.setCity("Basel");
        address.setState("BS");
        address.setStreet("Peter-Merianstrasse 41");
        address.setZip("4001");

        Phone phoneWork = new Phone();
        phoneWork.setPhonenumber("061 123 45 67");
        phoneWork.setType("Work");

        Employee emp = new Employee();
        emp.setAddress(address);
        emp.getPhones().add(phoneWork);
        emp.setName("John Doe");
        emp.setSalary(100000);
        phoneWork.setEmployee(emp);
        emp = employeeRepository.save(emp);

        return emp;
    }

    private Employee createEmployeeMaxMustermann() {
        Address address = new Address();
        address.setCity("Aarau");
        address.setState("AG");
        address.setStreet("Bahnhofstrasse 41");
        address.setZip("5001");

        Phone phoneWork = new Phone();
        phoneWork.setPhonenumber("062 888 45 67");
        phoneWork.setType("Work");

        Employee emp = new Employee();
        emp.setAddress(address);
        emp.getPhones().add(phoneWork);
        emp.setName("Max Mustermann");
        emp.setSalary(90000);
        phoneWork.setEmployee(emp);
        emp = employeeRepository.save(emp);

        return emp;
    }


    private void createPhone(Long empId) {
        Phone phoneWork = new Phone();
        phoneWork.setPhonenumber("031 999 99 99");
        phoneWork.setType("Work");

        Phone phoneHome = new Phone();
        phoneHome.setPhonenumber("032 333 33 33");
        phoneHome.setType("Home");

        employeeRepository.findByIdWithEagerRelationships(empId).ifPresent(emp -> {
            phoneWork.setEmployee(emp);
            emp.getPhones().add(phoneWork);

            phoneHome.setEmployee(emp);
            emp.getPhones().add(phoneHome);

            employeeRepository.save(emp);
        });
    }

    private void createDepartment() {

        Department departmentHR = new Department();
        departmentHR.setName("HR");
        departmentHR = departmentRepository.save(departmentHR);

        Department department = new Department();
        department.setName("Dev");
        Department departmentDev = departmentRepository.save(department);

        employeeRepository.findAllWithEagerRelationships().forEach(emp -> {
            departmentDev.getEmployees().add(emp);
            emp.setDepartment(departmentDev);
            employeeRepository.save(emp);
        });

        departmentRepository.findByIdWithEagerRelationships(departmentDev.getId()).ifPresent(dep -> {
            dep.getEmployees().forEach(e -> getLogger().info("Department " + dep.getName() + " - " + e.toString()));
        });

    }

    private void createDesignProject() {

        DesignProject project = new DesignProject("Arcos");
        project.setInnovationLevel(3);

        employeeRepository.findAllWithEagerRelationships().forEach(emp -> {
            project.getEmployees().add(emp);
            emp.getProjects().add(project);
        });
        projectRepository.save(project);
    }

    private void createQualityProject() {

        QualityProject project = new QualityProject("My Quality Project");

        employeeRepository.findAllWithEagerRelationships().forEach(emp -> {
            project.getEmployees().add(emp);
            emp.getProjects().add(project);
        });
        projectRepository.save(project);
    }

    private void assignMaxMustermannToDesignProject() {

        employeeRepository.findByNameWithEagerRelationships("Max Mustermann").ifPresent(employee -> {
            projectRepository.findWithEagerProjectByName("Arcos").forEach(project -> {
                project.getEmployees().add(employee);
                employee.getProjects().add(project);
                projectRepository.save(project);
            });
        });
    }

    private void assignMaxMustermannAsChef() {
        employeeRepository.findByNameWithEagerRelationships("Max Mustermann").ifPresent(chef -> {
            employeeRepository.findByNameWithEagerRelationships("Felix Muster").ifPresent(employee -> {
                employee.setChef(chef);
                chef.getEmployees().add(employee);
                employeeRepository.save(chef);
            });
        });
    }
}
