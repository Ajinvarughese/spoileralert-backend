package com.major.spoileralert.controller;

import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.service.AdminService;
import com.major.spoileralert.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    AdminService departmentAdminService;
    @Autowired
    DepartmentService departmentService;

    @PutMapping("/approve/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable Long userId,@RequestParam("approved-by") Long approvedBy ) throws EntityNotFoundException {
        return ResponseEntity.ok(departmentAdminService.approveUser(userId,approvedBy));
    }

    @PutMapping("/suspend/{userId}")
    public ResponseEntity<String> suspendUser(@PathVariable Long userId) throws EntityNotFoundException {
        return ResponseEntity.ok(departmentAdminService.suspendUser(userId));
    }

    @PutMapping("/reinstate/{userId}")
    public ResponseEntity<String> reinstateUser(@PathVariable Long userId) throws EntityNotFoundException {
        return ResponseEntity.ok(departmentAdminService.reinstateUser(userId));
    }

    @PutMapping("/update-code/{departmentId}")
    public ResponseEntity<String> updateDepartmentCode(@PathVariable Long departmentId, @RequestParam(value = "new-code") String newCode) throws EntityNotFoundException {
        return ResponseEntity.ok(departmentService.updateDepartmentCode(departmentId, newCode));
    }

}
