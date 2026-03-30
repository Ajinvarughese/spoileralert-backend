package com.major.spoileralert.controller;

import com.major.spoileralert.dto.*;
import com.major.spoileralert.dto.AuthResponseDto;
import com.major.spoileralert.dto.LoginRequestDto;
import com.major.spoileralert.dto.SignUpRequestDto;
import com.major.spoileralert.dto.UpdateUserRequestDto;
import com.major.spoileralert.entity.User;
import com.major.spoileralert.enumeration.UserRole;
import com.major.spoileralert.enumeration.UserStatus;
import com.major.spoileralert.exception.AuthenticationException;
import com.major.spoileralert.exception.DuplicateResourceException;
import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Handles user signup.
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto request)
            throws EntityNotFoundException, DuplicateResourceException {
        String responseMessage = authService.signUp(request);
        return ResponseEntity.ok(responseMessage);
    }

    /**
     * Handles user login and returns an access & refresh token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws AuthenticationException {
        AuthResponseDto authResponseDto = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        return ResponseEntity.ok(authResponseDto);
    }

    /**
     * Sends an email verification link.
     */
    @PostMapping("/send-verification-email")
    public ResponseEntity<String> sendVerificationEmail(@RequestParam String email) {
        String responseMessage = authService.sendVerificationEmail(email);
        return ResponseEntity.ok(responseMessage);
    }

    /**
     * Handles email verification.
     */
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        String html = authService.verifyEmail(token);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws EntityNotFoundException {
        User user = authService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/departments/{id}/users")
    public ResponseEntity<List<User>> getAllUsersByDepartmentId(@PathVariable Long id, @RequestParam(value = "user-role", required = false) UserRole userRole) {
        List<User> users = null;
        if (userRole != null) {
            users = authService.getAllUsersByDepartmentIdAndUserRole(id, userRole);
        } else{
            users = authService.getAllUsersByDepartmentId(id);
        }
        return ResponseEntity.ok(users);
    }

    /**
     * Handles forgot password request.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String responseMessage = authService.forgotPassword(email);
        return ResponseEntity.ok(responseMessage);
    }

    /**
     * Handles password reset.
     */
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        String responseMessage = authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) throws EntityNotFoundException {
        User user = authService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("users/{userId}/profile")
    public ResponseEntity<String> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequestDto updateUserRequestDto) throws EntityNotFoundException, DuplicateResourceException {
        String response = authService.updateUserProfile(userId, updateUserRequestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("users/{userId}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam UserStatus status) throws EntityNotFoundException {
        String response = authService.updateUserStatus(userId, status);
        return ResponseEntity.ok(response);
    }

}
