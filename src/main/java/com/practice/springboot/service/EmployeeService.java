package com.practice.springboot.service;

import com.practice.springboot.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee (Employee employee);
    List<Employee> getAllEmployees();
}
