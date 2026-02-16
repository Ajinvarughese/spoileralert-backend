package com.major.spoileralert.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "subcategory_department")
public class SubcategoryDepartment extends Audit{

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;  // The Subcategory Entity

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;  // The Department Entity

}
