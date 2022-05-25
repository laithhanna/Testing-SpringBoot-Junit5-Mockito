package com.practice.springboot.repository;

import com.practice.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/*There is no need to annotate this interface with @Repository.
* Our interface extends JpaRepository and,
* There is a class called "SimpleJpaRepository" which implements the JpaRepository interface.
* That class is internally annotated with @Repository.*/

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //visit this documentation for creating JPA queries using method names:
    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    Optional<Employee> findByEmail(String email);

    //define custom query using JPQL with index params
    @Query("select e from Employee e where e.firstName  = ?1 and e.lastName = ?2")
    Employee findEmployeeByFirstLastName(String firstName, String lastName);


    //define custom query using JPQL with named params
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName,@Param("lastName") String lastName);
}
