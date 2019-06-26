package com.tsukhu.demo.domain;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    private String name;
    private Integer salary;
    private Boolean isActive;
    private Integer departmentId;
}
