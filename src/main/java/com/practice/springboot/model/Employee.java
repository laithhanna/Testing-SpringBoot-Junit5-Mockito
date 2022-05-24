package com.practice.springboot.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Employee {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
}
