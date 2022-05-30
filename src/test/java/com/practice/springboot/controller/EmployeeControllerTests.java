package com.practice.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springboot.model.Employee;
import com.practice.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

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
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0)); //will return the same object we pass


        //when - action or the behavior we are testing
        //make a post rest api call. pass the content type as JSON. Content body is employee JSON object
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))); //serialize java object to JSON

        //then - validate the response of the rest api
        //validate that the response contains valid status code. (In this case CREATED aka 201).
        //Then, validate that the response contain valid JSON values.
        response.andDo(MockMvcResultHandlers.print()) //to print the rest api call and response on the terminal
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
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

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(List.of(employee, employee2));

        //when - action or the behavior we are testing
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)));
                //since the return is an array of JSON. $ represent an array of JSON so $.size give you size of that array

    }

}
