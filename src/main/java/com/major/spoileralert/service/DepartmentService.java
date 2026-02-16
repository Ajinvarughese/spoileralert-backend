package com.major.spoileralert.service;

import com.major.spoileralert.entity.Department;
import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) throws EntityNotFoundException {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
    }

    public Department getDepartmentByIdAndDepartmentCode(Long id, String departmentCode) throws EntityNotFoundException {
        return departmentRepository.findByIdAndDepartmentCode(id, departmentCode)
                .orElseThrow(() -> new EntityNotFoundException("No department with the given code exists"));
    }

    public String updateDepartmentCode(Long departmentId, String newCode) throws EntityNotFoundException {
        Department department = getDepartmentById(departmentId);

        department.setDepartmentCode(newCode);
        departmentRepository.save(department);
        return "Department code updated successfully.";
    }

    public String updateCode(Long departmentId, String code) throws EntityNotFoundException {
        Department department = getDepartmentById(departmentId);
        department.setDepartmentCode(code);
        departmentRepository.save(department);
        return "Department code updated successfully";
    }
}
