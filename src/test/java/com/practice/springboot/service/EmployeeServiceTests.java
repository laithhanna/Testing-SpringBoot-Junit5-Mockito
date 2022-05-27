package com.practice.springboot.service;

import com.practice.springboot.model.Employee;
import com.practice.springboot.repository.EmployeeRepository;
import com.practice.springboot.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) //this is used to tell Mockito that we are using Mockito annotations to mock the dependencies
public class EmployeeServiceTests { //basically we want to extend our class behavior from MockitoExtension class

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks //@InjectMocks creates the mock object of the class and inject the mocks that are marked with @Mock into it
    private EmployeeServiceImpl employeeService;

    /*
    Note for me:
    Since EmployeeService depends on EmployeeRepository, when testing the EmployeeService we need to mock the employeeRepository
    inside the EmployeeService. Now, instead of using the Mockito.mock() method to mock the EmployeeRepository, we can use the
    @Mock annotation. and instead of using constructor based dependency injection to inject the mocked EmployeeRepository object
    into the EmployeeService object as shown in the commented section of the setup method, we can use @InjectMocks annotation.
    */
    @BeforeEach
    public void setup() {
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
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
