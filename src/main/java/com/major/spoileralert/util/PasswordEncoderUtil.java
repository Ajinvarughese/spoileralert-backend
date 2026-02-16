package com.major.spoileralert.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Encrypt a password
        String rawPassword = "admin123";
        String encryptedPassword = encoder.encode(rawPassword);

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encrypted Password: " + encryptedPassword);

        // Verify the password (instead of decoding)
        boolean isMatch = encoder.matches(rawPassword, encryptedPassword);
        System.out.println("Password matches: " + isMatch);
    }
}
