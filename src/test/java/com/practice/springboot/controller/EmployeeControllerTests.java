package com.practice.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springboot.model.Employee;
import com.practice.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
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

    //JUnit test for createEmployee method
    @DisplayName("JUnit test for createEmployee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();

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

}
