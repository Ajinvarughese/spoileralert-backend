package com.major.spoileralert.dto;

import com.major.spoileralert.entity.Department;
import com.major.spoileralert.enumeration.UserRole;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String phone;

    // role can be "public_registered" or "department_incharge"
    @Enumerated(value = EnumType.STRING)
    @NotBlank(message = "Role is required")
    private UserRole role;

    // Optional: Only required if role is department_incharge
    private Department department;

    // Optional: Only required if role is department_incharge
    private String departmentCode;

}