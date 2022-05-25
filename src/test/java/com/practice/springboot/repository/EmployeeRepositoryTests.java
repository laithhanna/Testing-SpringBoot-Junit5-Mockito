package com.practice.springboot.repository;

import com.practice.springboot.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


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

    //JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior we are testing
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("Johnson");
        savedEmployee.setEmail("johnson@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Johnson");
        assertThat(updatedEmployee.getEmail()).isEqualTo("johnson@gmail.com");
    }

    //JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior we are testing
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    //JUnit test for custom query using JPQL with index params
    @DisplayName("JUnit test for custom query using JPQL with index params")
    @Test
    public void givenEmployeeFirstAndLastNames_whenFindByJPQLIndexParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = "John";
        String lastName = "Cena";

        //when - action or the behavior we are testing
        Employee employeeDB = employeeRepository.findByJPQLIndexParams(firstName, lastName);

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //JUnit test for custom query using JPQL with named params
    @DisplayName("JUnit test for custom query using JPQL with named params")
    @Test
    public void givenEmployeeFirstAndLastNames_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeRepository.save(employee);
        String firstName = "John";
        String lastName = "Cena";

        //when - action or the behavior we are testing
        Employee employeeDB = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //JUnit test for custom query using Native SQL with index params
    @DisplayName("JUnit test for custom query using Native SQL with index params")
    @Test
    public void givenEmployeeFirstAndLastNames_whenFindByNativeSQLIndexParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior we are testing
        Employee employeeDB = employeeRepository.findByNativeSQLIndexParams(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //JUnit test for custom query using Native SQL with named params
    @DisplayName("JUnit test for custom query using Native SQL with named params")
    @Test
    public void givenEmployeeFirstAndLastNames_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior we are testing
        Employee employeeDB = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }
}
