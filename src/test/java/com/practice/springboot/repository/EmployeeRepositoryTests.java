package com.practice.springboot.repository;

import com.practice.springboot.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    //JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

       //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        //when - action or the behavior we are testing
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //JUnit test for get all employees operation
    @DisplayName("JUnit test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenReturnEmployeesList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Will")
                .lastName("Smith")
                .email("will@gmail.com")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when - action or the behavior we are testing
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //JUnit test for get employee by id operation
    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior we are testing
        Employee employeeDB = employeeRepository.findById(employee.getId()).get(); //since findById return Optional we need to use .get() method

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior we are testing
        Employee employeeDB = employeeRepository.findByEmail("john@gmail.com").get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }
}
