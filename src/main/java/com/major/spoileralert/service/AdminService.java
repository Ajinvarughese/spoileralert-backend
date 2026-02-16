package com.major.spoileralert.service;

import com.major.spoileralert.entity.User;
import com.major.spoileralert.enumeration.UserStatus;
import com.major.spoileralert.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AdminService {

    @Autowired
    AuthService authService;

    public String approveUser(Long userId, Long approvedByUserId) throws EntityNotFoundException {
        User user = authService.getUserById(userId);

        User approvedByUser = authService.getUserById(approvedByUserId);

        switch (user.getStatus()) {
            case ACTIVE -> {
                return "The user is already approved and active.";
            }
            case SUSPENDED -> {
                return "This user has been suspended and cannot be approved.";
            }
            case REJECTED, PENDING -> {
                user.setStatus(UserStatus.ACTIVE);
                user.setApprovedBy(approvedByUser); // Store who approved
                authService.saveUser(user);

                // Log the approval action
                log.info("User {} has been approved by Admin {}", userId, approvedByUserId);
                return "User approved successfully by " + approvedByUser.getFullName();
            }
            default -> {
                return "Invalid user status. Please check the user's details and try again.";
            }
        }
    }

    public String suspendUser(Long userId) throws EntityNotFoundException {
        User user = authService.getUserById(userId);

        if (user.getStatus() == UserStatus.SUSPENDED) {
            return "User is already suspended.";
        }

        user.setStatus(UserStatus.SUSPENDED);
        authService.saveUser(user);
        return "User suspended successfully.";
    }

    public String reinstateUser(Long userId) throws EntityNotFoundException {
        User user = authService.getUserById(userId);

        if (user.getStatus() != UserStatus.SUSPENDED) {
            return "User is not suspended.";
        }

        user.setStatus(UserStatus.ACTIVE);
        authService.saveUser(user);
        return "User reinstated successfully.";
    }

}
