package com.major.spoileralert.controller;

import com.major.spoileralert.entity.Subcategory;
import com.major.spoileralert.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubcategoryController {

    @Autowired
    SubcategoryService subcategoryService;

    @GetMapping(value = "/categories/{id}/subcategories")
    public List<Subcategory> getAllSubcategoriesByCategoryId(@PathVariable Long id) {
        return subcategoryService.getAllSubcategoriesByCategoryId(id);
    }

}
