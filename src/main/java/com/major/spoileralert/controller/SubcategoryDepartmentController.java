package com.major.spoileralert.controller;

import com.major.spoileralert.entity.Subcategory;
import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.service.SubcategoryDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubcategoryDepartmentController {

    @Autowired
    SubcategoryDepartmentService subcategoryDepartmentService;

    @GetMapping(value = "/departments/{id}/subcategories")
    public List<Subcategory> getAllSubcategoriesByDepartmentId(@PathVariable Long id) throws EntityNotFoundException {
        return subcategoryDepartmentService.getAllSubcategoriesByDepartmentId(id);
    }

}
