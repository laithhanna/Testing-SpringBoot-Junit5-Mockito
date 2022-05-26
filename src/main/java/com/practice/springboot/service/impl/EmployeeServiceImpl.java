package com.practice.springboot.service.impl;

import com.practice.springboot.exception.ResourceNotFoundException;
import com.practice.springboot.model.Employee;
import com.practice.springboot.repository.EmployeeRepository;
import com.practice.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        //if the employee email is not unique then throw an exception
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with the given email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }
}
