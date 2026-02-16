package com.major.spoileralert.repository;

import com.major.spoileralert.entity.Department;
import com.major.spoileralert.entity.Subcategory;
import com.major.spoileralert.entity.SubcategoryDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubcategoryDepartmentRepository extends JpaRepository<SubcategoryDepartment, Long> {
    Optional<SubcategoryDepartment> findBySubcategory(Subcategory subcategory);

    Optional<List<SubcategoryDepartment>> findByDepartment(Department department);

}
