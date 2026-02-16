package com.major.spoileralert.dto;

import com.major.spoileralert.entity.Department;
import com.major.spoileralert.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentUserDto {

    private Department department;

    private List<User> admins;

    private List<User> incharges;

}