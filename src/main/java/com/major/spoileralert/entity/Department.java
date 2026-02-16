package com.major.spoileralert.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "department")
public class Department extends Audit {

    @Column(name = "department_code", nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "name", nullable = false)
    private String name;

}
