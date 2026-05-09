package com.studentapp.util;

import com.studentapp.model.Student;
import java.util.regex.Pattern;

/**
 * Utility class for validating student data
 * This demonstrates utility package usage and validation patterns
 */
public class StudentValidator {

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    // Phone number validation pattern (simple format)
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\d{3}-\\d{4}$|^\\d{10}$");
    
    /**
     * Validate complete student object
     */
    public static boolean isValidStudent(Student student) {
        if (student == null) {
            return false;
        }
        
        return isValidName(student.getFirstName()) &&
               isValidName(student.getLastName()) &&
               isValidEmail(student.getEmail()) &&
               isValidPhoneNumber(student.getPhoneNumber()) &&
               isValidDepartment(student.getDepartment()) &&
               isValidGpa(student.getGpa());
    }
    
    /**
     * Validate first name or last name
     */
    public static boolean isValidName(String name) {
        return name != null && 
               !name.trim().isEmpty() && 
               name.length() >= 2 && 
               name.length() <= 50 &&
               name.matches("^[a-zA-Z\\s'-]+$");
    }
    
    /**
     * Validate email address
     */
    public static boolean isValidEmail(String email) {
        return email != null && 
               !email.trim().isEmpty() && 
               EMAIL_PATTERN.matcher(email).matches() &&
               email.length() <= 100;
    }
    
    /**
     * Validate phone number
     */
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && 
               !phone.trim().isEmpty() && 
               PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validate department name
     */
    public static boolean isValidDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            return false;
        }
        
        String[] validDepartments = {
            "Computer Science", "Mathematics", "Physics", "Chemistry", 
            "Biology", "Engineering", "Business", "Economics", 
            "Literature", "History", "Psychology", "Sociology"
        };
        
        for (String validDept : validDepartments) {
            if (validDept.equalsIgnoreCase(department.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate GPA (should be between 0.0 and 4.0)
     */
    public static boolean isValidGpa(double gpa) {
        return gpa >= 0.0 && gpa <= 4.0;
    }
    
    /**
     * Get validation error message for student
     */
    public static String getValidationErrorMessage(Student student) {
        StringBuilder errors = new StringBuilder();
        
        if (!isValidName(student.getFirstName())) {
            errors.append("Invalid first name. Must be 2-50 characters, letters only.\n");
        }
        
        if (!isValidName(student.getLastName())) {
            errors.append("Invalid last name. Must be 2-50 characters, letters only.\n");
        }
        
        if (!isValidEmail(student.getEmail())) {
            errors.append("Invalid email address.\n");
        }
        
        if (!isValidPhoneNumber(student.getPhoneNumber())) {
            errors.append("Invalid phone number. Use format: 555-1234 or 5551234\n");
        }
        
        if (!isValidDepartment(student.getDepartment())) {
            errors.append("Invalid department. Must be one of the approved departments.\n");
        }
        
        if (!isValidGpa(student.getGpa())) {
            errors.append("Invalid GPA. Must be between 0.0 and 4.0\n");
        }
        
        return errors.toString();
    }
    
    /**
     * Format phone number to standard format
     */
    public static String formatPhoneNumber(String phone) {
        if (phone == null) return "";
        
        // Remove all non-digit characters
        String digits = phone.replaceAll("[^0-9]", "");
        
        // Format as 555-1234 if exactly 7 digits
        if (digits.length() == 7) {
            return digits.substring(0, 3) + "-" + digits.substring(3);
        }
        
        return phone;
    }
    
    /**
     * Capitalize name properly
     */
    public static String capitalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        
        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                           .append(word.substring(1));
                if (word != words[words.length - 1]) {
                    capitalized.append(" ");
                }
            }
        }
        
        return capitalized.toString();
    }
}
