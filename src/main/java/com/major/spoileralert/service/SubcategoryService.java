package com.major.spoileralert.service;

import com.major.spoileralert.entity.Subcategory;
import com.major.spoileralert.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryService {

    @Autowired
    SubcategoryRepository subcategoryRepository;

    public List<Subcategory> getAllSubcategoriesByCategoryId(Long id) {
        return subcategoryRepository.findByCategoryId(id);
    }

}
