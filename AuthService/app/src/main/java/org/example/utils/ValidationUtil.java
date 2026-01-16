package org.example.utils;

import java.util.regex.Pattern;
public class ValidationUtil {

    // Reasonable, maintainable email regex
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    public static void validateEmail(String email){

        if(email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // 2. Normalize
        String normalizedEmail = email.trim().toLowerCase();

        // 3. Format check
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static void validatePassword(String password) {

        // 1. Null or blank check
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // 2. No whitespace allowed
        if (password.contains(" ")) {
            throw new IllegalArgumentException("Password must not contain spaces");
        }

        // 3. Strength check
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters long and contain " +
                            "uppercase, lowercase, digit, and special character"
            );
        }
    }
}
