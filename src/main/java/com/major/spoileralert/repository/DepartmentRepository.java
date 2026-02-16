package com.major.spoileralert.repository;

import com.major.spoileralert.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByIdAndDepartmentCode(Long id, String departmentCode);

}
