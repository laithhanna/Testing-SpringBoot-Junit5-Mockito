package com.practice.springboot.controller;

import com.practice.springboot.model.Employee;
import com.practice.springboot.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    //constructor based dependency injection
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId) {
        //ResponseEntity represents the whole HTTP response: status code, headers, and body.
        // As a result, we can use it to fully configure the HTTP response.
        return employeeService.getEmployeeById(employeeId)
                .map(ResponseEntity::ok) //if id is valid return Ok status code
                .orElseGet(() -> ResponseEntity.notFound().build()); //if id is not valid return 404 notFound status
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Employee employee) {
        return employeeService.getEmployeeById(employeeId).map(savedEmployee -> {
            savedEmployee.setFirstName(employee.getFirstName());
            savedEmployee.setLastName(employee.getLastName());
            savedEmployee.setEmail(employee.getEmail());

            Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<String>("Employee deleted successfully!.", HttpStatus.OK);
    }
}
