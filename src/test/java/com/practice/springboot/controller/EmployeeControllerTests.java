package com.practice.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springboot.model.Employee;
import com.practice.springboot.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc; //MockMvc class is used to call Rest APIs

    /*Note to me:
    @MockBean annotation tells Spring to create a mock instance of the class/interface
     and add it to the application context so that it is injected to the class's object we are testing.
     Here, it will create a mock instance of EmployeeService and add it to the application context
     so that it is injected into EmployeeController and be available to its Rest apis
    */
    @MockBean //@MockBean is often used with @WebMvcTest.
    private EmployeeService employeeService;

    //To serialize and deserialize java objects
    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
    }

    //JUnit test for createEmployee method
    @DisplayName("JUnit test for createEmployee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{
        //given - precondition or setup
        //employee object is created in the setup method

        //stub the employeeService.saveEmployee() method
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0)); //will return the same object we pass


        //when - action or the behavior we are testing
        //make a post rest api call. pass the content type as JSON. Content body is employee JSON object
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))); //serialize java object to JSON

        //then - validate the response of the rest api
        //validate that the response contains valid status code. (In this case CREATED aka 201).
        //Then, validate that the response contain valid JSON values.
        response.andDo(print()) //to print the rest api call and response on the terminal
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
        //note that the '$' symbol represent the root object aka the whole JSON object
    }

    //JUnit test for getAllEmployees REST api
    @DisplayName("JUnit test for getAllEmployees REST api")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception{
        //given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("Will")
                .lastName("Smith")
                .email("will@gmail.com")
                .build();

        given(employeeService.getAllEmployees()).willReturn(List.of(employee, employee2));

        //when - action or the behavior we are testing
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
                //since the return is an array of JSON. $ represent an array of JSON so $.size give you size of that array

    }

    //JUnit test for getEmployeeById REST api - positive scenario
    @DisplayName("JUnit test for getEmployeeById REST api - positive scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnsEmployeeObject() throws Exception{
        //given - precondition or setup
        long employeeId = 1L;
        //employee object will be created in the setup method
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behavior we are testing
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    //JUnit test for getEmployeeById REST api - positive scenario
    @DisplayName("JUnit test for getEmployeeById REST api - negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnsEmpty() throws Exception{
        //given - precondition or setup
        long employeeId = 1L;
        //employee object will be created in the setup method
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behavior we are testing
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    //JUnit test for updateEmployee REST api - positive scenario
    @DisplayName("JUnit test for updateEmployee REST api - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{
        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("Johnson")
                .lastName("C")
                .email("johnson@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behavior we are testing
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    //JUnit test for updateEmployee REST api - positive scenario
    @DisplayName("JUnit test for updateEmployee REST api - negative scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("Johnson")
                .lastName("C")
                .email("johnson@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behavior we are testing
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

}
