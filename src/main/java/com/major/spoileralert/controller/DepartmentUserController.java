package com.major.spoileralert.controller;

import com.major.spoileralert.dto.DepartmentUserDto;
import com.major.spoileralert.service.DepartmentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments/users")
public class DepartmentUserController {

    @Autowired
    DepartmentUserService departmentUserService;

    @GetMapping("/overview")
    public ResponseEntity<List<DepartmentUserDto>> getAllDepartmentsWithUsers() {
        return ResponseEntity.ok(departmentUserService.getAllDepartmentsWithUsers());
    }
}
