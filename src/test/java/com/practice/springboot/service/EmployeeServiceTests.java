package com.practice.springboot.service;

import com.practice.springboot.exception.ResourceNotFoundException;
import com.practice.springboot.model.Employee;
import com.practice.springboot.repository.EmployeeRepository;
import com.practice.springboot.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private  Employee employee;

    @BeforeEach
    public void setup() {
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
    }

    //JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        /* We need to use stubbing:
        Using stubbing we train the mock objects about what values to return when its methods are invoked.
        Mockito provides when–then (or in BDD style given-will) stubbing pattern to stub a mock object’s method invocation.
        */
        //given - precondition or setup
            //we need to stub 2 method calls.
            //The saveEmployee method internally uses 2 employeeRepository methods, "findByEmail" and "save"

        //when invoking findByEmail method, it should return empty Optional
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        //when invoking save method, it should return the saved employee object
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behavior we are testing
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behavior we are testing
        //assertThrows(expected type, Executable executable "aka lambda expression")
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then - verify that we will never reach the save() method because we have thrown the exception
        //verify(mocked object, #of invocations of the mocked object's method).method()
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
