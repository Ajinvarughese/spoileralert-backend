package com.major.spoileralert.entity;

import com.major.spoileralert.enumeration.UserRole;
import com.major.spoileralert.enumeration.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "app_user")
public class User extends Audit {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "user_name", unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String passwordHash;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_date")
    private Timestamp approvedDate;

    @Column(name = "verified", nullable = false)
    private boolean verified = false;  // Email verification status

    @Column(name = "verification_token", unique = true)
    private String verificationToken;

    @Column(name = "reset_token", unique = true)
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private Instant resetTokenExpiry; // Token expiry time
}
