package com.practice.springboot.service.impl;

import com.practice.springboot.exception.EmployeeIdNotFoundException;
import com.practice.springboot.exception.ResourceNotFoundException;
import com.practice.springboot.model.Employee;
import com.practice.springboot.repository.EmployeeRepository;
import com.practice.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    //@Autowired
    // I will use the constructor based dependency injection approach to inject EmployeeRepository in the EmployeeServiceImpl objects
    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        //if the employee email is not unique then throw an exception
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with the given email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new EmployeeIdNotFoundException("Provided employee id is not found: " + id);
        }
        return employee;
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }
}
