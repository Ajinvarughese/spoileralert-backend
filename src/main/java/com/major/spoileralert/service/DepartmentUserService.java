package com.major.spoileralert.service;

import com.major.spoileralert.dto.DepartmentUserDto;
import com.major.spoileralert.entity.Department;
import com.major.spoileralert.entity.User;
import com.major.spoileralert.enumeration.UserRole;
import com.major.spoileralert.repository.DepartmentRepository;
import com.major.spoileralert.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentUserService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    UserRepository userRepository;

    public List<DepartmentUserDto> getAllDepartmentsWithUsers() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream().map(dept -> {
            // Fetch all admins for the department (could be multiple admins)
            List<User> admins = userRepository
                    .findByDepartmentIdAndRole(dept.getId(), UserRole.DEPARTMENT_ADMIN);

            // Fetch in-charges for the department (could be multiple in-charges)
            List<User> incharges = userRepository
                    .findByDepartmentIdAndRole(dept.getId(), UserRole.DEPARTMENT_INCHARGE);

            DepartmentUserDto departmentUserDto = new DepartmentUserDto();
            departmentUserDto.setDepartment(dept);
            departmentUserDto.setAdmins(admins);
            departmentUserDto.setIncharges(incharges);

            // Return the DTO with the department and user details
            return departmentUserDto;

        }).collect(Collectors.toList());
    }

}
