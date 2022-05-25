package com.practice.springboot.repository;

import com.practice.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/*There is no need to annotate this interface with @Repository.
* Our interface extends JpaRepository and,
* There is a class called "SimpleJpaRepository" which implements the JpaRepository interface.
* That class is internally annotated with @Repository.*/

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    //define custom query using JPQL (Java Persistence Query Language) with index params
    @Query("select e from Employee e where e.firstName  = ?1 and e.lastName = ?2")
    Employee findEmployeeByFirstLastName(String firstName, String lastName);
}
