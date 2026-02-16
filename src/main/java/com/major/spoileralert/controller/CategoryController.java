package com.major.spoileralert.controller;

import com.major.spoileralert.entity.Category;
import com.major.spoileralert.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
    
}
