package com.practice.springboot.service;

import com.practice.springboot.model.Employee;
import com.practice.springboot.repository.EmployeeRepository;
import com.practice.springboot.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;


public class EmployeeServiceTests {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    /*
    Note for me:

    Since EmployeeService depends on EmployeeRepository, when testing the EmployeeService we need to mock the employeeRepository inside the EmployeeService.
    So, in the setup method below, we will mock an EmployeeRepository object. Also, since we are testing EmployeeService, we need
    to create an EmployeeService object and inject the employeeRepository mocking object in it.
    Now, we are ready to test the EmployeeService methods.
    - Note that I used constructor based dependency injection approach to inject the EmployeeRepository object in the EmployeeService object
    */
    @BeforeEach
    public void setup() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    //JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        /*
        Stubbing go in the given section.
        Using stubbing we train the mock objects about what values to return when its methods are invoked.
        Mockito provides when–then (or in BDD style given-will) stubbing pattern to stub a mock object’s method invocation.
        */

        //given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();

        //we need to stub 2 method calls.
        //The saveEmployee method uses 2 employeeRepository methods, "findByEmail" and "save"

        //when invoking findByEmail method, it should return empty Optional
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        //when invoking save method, it should return the saved employee object
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behavior we are testing
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }
}
