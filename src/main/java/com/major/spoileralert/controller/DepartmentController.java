package com.major.spoileralert.controller;

import com.major.spoileralert.entity.Department;
import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "/departments")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping(value = "/departments/{id}")
    public Department getDepartmentById(@PathVariable Long id) throws EntityNotFoundException {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/departments/{departmentId}/update-code")
    public ResponseEntity<String> updateDepartmentCode(
            @PathVariable Long departmentId,
            @RequestParam String code) throws EntityNotFoundException {
        return ResponseEntity.ok(departmentService.updateCode(departmentId, code));
    }

}
