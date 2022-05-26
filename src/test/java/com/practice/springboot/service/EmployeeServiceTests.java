package com.practice.springboot.service;

import com.practice.springboot.repository.EmployeeRepository;
import com.practice.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;


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
        /**/
    }
}
