package com.major.spoileralert.service;

import com.major.spoileralert.dto.AuthResponseDto;
import com.major.spoileralert.dto.SignUpRequestDto;
import com.major.spoileralert.dto.UpdateUserRequestDto;
import com.major.spoileralert.entity.Department;
import com.major.spoileralert.entity.User;
import com.major.spoileralert.enumeration.UserRole;
import com.major.spoileralert.enumeration.UserStatus;
import com.major.spoileralert.exception.AuthenticationException;
import com.major.spoileralert.exception.DuplicateResourceException;
import com.major.spoileralert.exception.EntityNotFoundException;
import com.major.spoileralert.repository.UserRepository;
import com.major.spoileralert.util.Utils;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    /**
     * Handles user signup and prevents unauthorized roles from registering manually.
     */
    public String signUp(SignUpRequestDto request) throws EntityNotFoundException, DuplicateResourceException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email is already in use.");
        }
        if (userRepository.findByUsername(request.getUserName()).isPresent()) {
            throw new DuplicateResourceException("Username is already taken.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        if (request.getRole() == UserRole.DEPARTMENT_INCHARGE) {

            Department department = departmentService.getDepartmentByIdAndDepartmentCode(request.getDepartment().getId(), request.getDepartmentCode());

            user.setStatus(UserStatus.PENDING);
            user.setDepartment(department);
        } else {
            user.setStatus(UserStatus.ACTIVE);
        }

        saveUser(user);
        return "User registered successfully. " +
                (request.getRole() == UserRole.DEPARTMENT_INCHARGE ? "Awaiting approval." : "");
    }

    /**
     * Authenticates a user.
     */
    public AuthResponseDto login(String email, String password) throws AuthenticationException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Invalid credentials."));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid credentials.");
        }

        if (user.getStatus() != null) {
            // Check if user is waiting for approval
            if (user.getStatus() == UserStatus.PENDING) {
                throw new AuthenticationException("Account is pending approval.");
            } else if (user.getStatus() == UserStatus.SUSPENDED) {
                throw new AuthenticationException("Account is suspended.");
            } else if (user.getStatus() == UserStatus.REJECTED) {
                throw new AuthenticationException("Account is rejected.");
            }
        }

        // Ensure email is verified before allowing login
        if (!user.isVerified()) {
            throw new AuthenticationException("Email verification required.");
        }

        return new AuthResponseDto("Login successful!", user.getId());
    }

    private String generateVerificationMessage(String token) {
        String verificationLink = "http://localhost:8080/verify-email?token=" + token;

        return "<html><body>"
                + "<h2>Hello,</h2>"
                + "<p>Thank you for signing up with Spoiler Alert! To complete your registration, please verify your email address by clicking the button below:</p>"
                + "<p><a href='" + verificationLink + "' "
                + "style='background-color: #008CBA; color: white; padding: 10px 20px; "
                + "text-decoration: none; border-radius: 5px; display: inline-block;'>"
                + "👉 Verify My Email</a></p>"
                + "<p>If the button doesn’t work, copy and paste this link into your browser:</p>"
                + "<p><a href='" + verificationLink + "'>" + verificationLink + "</a></p>"
                + "<p>If you didn’t sign up for an account, please ignore this email.</p>"
                + "<p>Best regards,<br><strong>Spoiler Alert Team</strong></p>"
                + "</body></html>";
    }

    /**
     * Sends a verification email with a unique token.
     */
    public String sendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        saveUser(user);

        try {
            String subject = "Verify your email for Spoiler Alert";
            String message = generateVerificationMessage(verificationToken);
            emailService.sendEmail(user.getEmail(), subject, message);
        } catch (MessagingException e) {
            log.info("Failed to send verification email: " + e.getMessage());
            return "Failed to send verification email.";
        }

        return "Verification email sent!";
    }

    /**
     * Verifies a user's email using a token.
     */
    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token."));

        user.setVerified(true);
        user.setVerificationToken(null);
        saveUser(user);
        return "Email verified successfully!";
    }

    /**
     * Sends a password reset email.
     */
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Generate a secure random token(OTP)
        String resetToken = Utils.generateOtp();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(Instant.now().plus(Duration.ofMinutes(30))); // Expire in 30 minutes
        saveUser(user);

        try {
            String subject = "Password Reset Request";
            String message = generateResetMessage(resetToken);

            // Send reset link email
            emailService.sendEmail(user.getEmail(), subject, message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send reset email.", e);
        }

        return "Password reset link sent!";
    }

    private String generateResetMessage(String otp) {
        return "<html><body>" +
                "<h2>Hello,</h2>" +
                "<p>We received a request to reset your password for your Community Care account.</p>" +
                "<p>Your One-Time Password (OTP) for resetting your password is:</p>" +
                "<h3 style='background-color: #10B981; color: white; padding: 10px 20px; " +
                "border-radius: 5px; display: inline-block;'>" + otp + "</h3>" +
                "<p>This OTP is valid for the next 30 minutes. If you didn’t request a password reset, you can safely ignore this email.</p>" +
                "<p>Best regards,<br><strong>Community Care Team</strong></p>" +
                "</body></html>";
    }

    /**
     * Resets a user's password using a token.
     */
    public String resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token."));

        // Check if the token is expired
        if (user.getResetTokenExpiry().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Reset token has expired.");
        }

        // Set the new password
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        saveUser(user);

        return "Password reset successfully!";
    }

    public User getUserById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsersByDepartmentIdAndUserRole(Long id, UserRole userRole) {
        return userRepository.findByDepartmentIdAndRole(id, userRole);
    }

    public List<User> getAllUsersByDepartmentId(Long id) {
        return userRepository.findByDepartmentId(id);
    }

    public String updateUserProfile(Long userId, UpdateUserRequestDto request) throws EntityNotFoundException, DuplicateResourceException {
        User user = getUserById(userId);

        // Check for email uniqueness if updating email
        if (!user.getEmail().equals(request.getEmail()) && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("The email " + request.getEmail() + " is already in use.");
        }

        // Check for username uniqueness if updating username
        if (user.getUsername() != null && !user.getUsername().equals(request.getUsername()) && userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("The username " + request.getUsername() + " is already taken.");
        }

        // Update user details
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        saveUser(user);
        return "User profile updated successfully.";
    }

    public String updateUserStatus(Long userId, UserStatus newStatus) throws EntityNotFoundException {
        User user = getUserById(userId);

        user.setStatus(newStatus);
        saveUser(user);

        return "User status updated to " + newStatus + ".";
    }

    public String getDepartmentAdminEmail(Long departmentId) {
        return userRepository.findByDepartmentIdAndRole(departmentId, UserRole.DEPARTMENT_ADMIN).stream()
                .findFirst()
                .map(User::getEmail)
                .orElse(null);  // Or throw an exception if required
    }

}