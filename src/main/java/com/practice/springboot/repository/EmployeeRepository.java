package com.practice.springboot.repository;

import com.practice.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/*There is no need to annotate this interface with @Repository.
* Our interface extends JpaRepository and,
* There is a class called "SimpleJpaRepository" which implements the JpaRepository interface.
* That class is internally annotated with @Repository.*/

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
