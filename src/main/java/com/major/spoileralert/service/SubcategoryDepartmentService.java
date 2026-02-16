package com.major.spoileralert.service;

import com.major.spoileralert.entity.Department;
import com.major.spoileralert.entity.Subcategory;
import com.major.spoileralert.entity.SubcategoryDepartment;
import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.repository.SubcategoryDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoryDepartmentService {

    @Autowired
    SubcategoryDepartmentRepository subcategoryDepartmentRepository;

    @Autowired
    DepartmentService departmentService;

    public SubcategoryDepartment getSubcategoryDepartmentBySubcategory(Subcategory subcategory) throws EntityNotFoundException {
        return subcategoryDepartmentRepository
                .findBySubcategory(subcategory)
                .orElseThrow(() -> new EntityNotFoundException("No department found for subcategory"));
    }

    public List<SubcategoryDepartment> getAllSubcategoryDepartmentsByDepartment(Department department) throws EntityNotFoundException {
        return subcategoryDepartmentRepository
                .findByDepartment(department)
                .orElseThrow(() -> new EntityNotFoundException("No subcategory found for department"));
    }

    public List<Subcategory> getAllSubcategoriesByDepartmentId(Long id) throws EntityNotFoundException {
        Department department = departmentService.getDepartmentById(id);
        List<SubcategoryDepartment> subcategoryDepartmentList = getAllSubcategoryDepartmentsByDepartment(department);

        // Extract subcategories from the list of SubcategoryDepartment
        return subcategoryDepartmentList.stream()
                .map(SubcategoryDepartment::getSubcategory) // Assuming SubcategoryDepartment has getSubcategory()
                .collect(Collectors.toList());
    }

}
