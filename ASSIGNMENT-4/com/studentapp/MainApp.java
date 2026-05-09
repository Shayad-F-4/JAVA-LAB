package com.studentapp;

import com.studentapp.model.Student;
import com.studentapp.service.StudentService;
import com.studentapp.util.StudentValidator;
import com.studentapp.util.StudentFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class demonstrating package usage
 * This is the entry point for the Student Management System
 * 
 * This application demonstrates:
 * 1. Package structure and organization
 * 2. Model-View-Service architecture
 * 3. Separation of concerns
 * 4. Utility classes and validation
 * 5. Business logic separation
 */
public class MainApp {
    private static StudentService studentService;
    private static Scanner scanner;

    public static void main(String[] args) {
        // Initialize services
        studentService = new StudentService();
        scanner = new Scanner(System.in);
        
        System.out.println(StudentFormatter.createHeader("STUDENT MANAGEMENT SYSTEM"));
        System.out.println("Package-based Java Application Demonstration");
        System.out.println("Developed for Assignment 4 - Package Usage");
        System.out.println();
        
        // Run demonstration
        runPackageDemo();
        
        // Interactive menu
        runInteractiveMenu();
        
        // Clean up
        scanner.close();
        System.out.println("\nThank you for using Student Management System!");
    }

    /**
     * Demonstrate package functionality
     */
    private static void runPackageDemo() {
        System.out.println(StudentFormatter.createHeader("PACKAGE DEMONSTRATION"));
        
        // Show current students
        System.out.println("Sample students loaded from service initialization:");
        System.out.println(studentService.generateStatisticsReport());
        
        // Demonstrate model package usage
        System.out.println(StudentFormatter.createHeader("MODEL PACKAGE DEMONSTRATION"));
        demonstrateModelPackage();
        
        // Demonstrate service package usage
        System.out.println(StudentFormatter.createHeader("SERVICE PACKAGE DEMONSTRATION"));
        demonstrateServicePackage();
        
        // Demonstrate utility package usage
        System.out.println(StudentFormatter.createHeader("UTILITY PACKAGE DEMONSTRATION"));
        demonstrateUtilityPackage();
    }

    /**
     * Demonstrate model package functionality
     */
    private static void demonstrateModelPackage() {
        System.out.println("Creating new Student using com.studentapp.model package:");
        
        Student newStudent = new Student();
        newStudent.setFirstName("Alice");
        newStudent.setLastName("Anderson");
        newStudent.setEmail("alice.anderson@university.edu");
        newStudent.setPhoneNumber("555-0107");
        newStudent.setDateOfBirth(LocalDate.of(2002, 5, 15));
        newStudent.setDepartment("Computer Science");
        newStudent.setGpa(3.6);
        
        System.out.println("Student object created successfully!");
        System.out.println(StudentFormatter.formatStudent(newStudent));
        System.out.println("Student methods demonstration:");
        System.out.println("  Full Name: " + newStudent.getFullName());
        System.out.println("  Age: " + newStudent.getAge() + " years");
        System.out.println("  Grade Level: " + newStudent.getGradeLevel());
    }

    /**
     * Demonstrate service package functionality
     */
    private static void demonstrateServicePackage() {
        System.out.println("Using com.studentapp.service package for business operations:");
        
        // Add the student we created
        Student addedStudent = studentService.addStudent("Alice", "Anderson", 
                "alice.anderson@university.edu", "555-0107", "Computer Science", 3.6);
        System.out.println("Student added via service: " + addedStudent.getFullName());
        
        // Search functionality
        List<Student> csStudents = studentService.getStudentsByDepartment("Computer Science");
        System.out.println("\nComputer Science students found: " + csStudents.size());
        
        // GPA operations
        List<Student> topStudents = studentService.getTopPerformingStudents();
        System.out.println("Top performing students: " + topStudents.size());
        
        // Statistics
        System.out.println("Average GPA: " + String.format("%.2f", studentService.calculateAverageGpa()));
    }

    /**
     * Demonstrate utility package functionality
     */
    private static void demonstrateUtilityPackage() {
        System.out.println("Using com.studentapp.util package for validation and formatting:");
        
        // Create a test student for validation
        Student testStudent = new Student();
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setEmail("invalid-email");
        testStudent.setPhoneNumber("123");
        testStudent.setDepartment("Invalid Department");
        testStudent.setGpa(5.0);
        
        // Validation demonstration
        System.out.println("Validation Results:");
        System.out.println("Is valid student: " + StudentValidator.isValidStudent(testStudent));
        System.out.println("Validation errors:");
        System.out.println(StudentValidator.getValidationErrorMessage(testStudent));
        
        // Formatting demonstration
        List<Student> allStudents = studentService.getAllStudents();
        System.out.println("\nFormatted Student Table:");
        System.out.println(StudentFormatter.formatStudentsAsTable(allStudents));
    }

    /**
     * Interactive menu for user interaction
     */
    private static void runInteractiveMenu() {
        while (true) {
            System.out.println(StudentFormatter.createHeader("INTERACTIVE MENU"));
            System.out.println("1. View All Students");
            System.out.println("2. Add New Student");
            System.out.println("3. Search Student by Name");
            System.out.println("4. View Students by Department");
            System.out.println("5. View Top Performing Students");
            System.out.println("6. View Students at Risk");
            System.out.println("7. View Statistics");
            System.out.println("8. Exit");
            System.out.print("Enter your choice (1-8): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewAllStudents();
                        break;
                    case 2:
                        addNewStudent();
                        break;
                    case 3:
                        searchStudentByName();
                        break;
                    case 4:
                        viewStudentsByDepartment();
                        break;
                    case 5:
                        viewTopPerformingStudents();
                        break;
                    case 6:
                        viewStudentsAtRisk();
                        break;
                    case 7:
                        viewStatistics();
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void viewAllStudents() {
        System.out.println(StudentFormatter.createHeader("ALL STUDENTS"));
        List<Student> students = studentService.getAllStudents();
        System.out.println(StudentFormatter.formatStudentsAsTable(students));
    }

    private static void addNewStudent() {
        System.out.println(StudentFormatter.createHeader("ADD NEW STUDENT"));
        
        try {
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Phone (555-1234 format): ");
            String phone = scanner.nextLine();
            
            System.out.print("Department: ");
            String department = scanner.nextLine();
            
            System.out.print("GPA (0.0-4.0): ");
            double gpa = Double.parseDouble(scanner.nextLine());
            
            Student newStudent = studentService.addStudent(firstName, lastName, email, phone, department, gpa);
            System.out.println("Student added successfully!");
            System.out.println(StudentFormatter.formatStudent(newStudent));
            
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private static void searchStudentByName() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();
        
        List<Student> results = studentService.searchStudentsByName(name);
        System.out.println(StudentFormatter.createHeader("SEARCH RESULTS"));
        
        if (results.isEmpty()) {
            System.out.println("No students found with name: " + name);
        } else {
            System.out.println("Found " + results.size() + " student(s):");
            System.out.println(StudentFormatter.formatStudentsAsTable(results));
        }
    }

    private static void viewStudentsByDepartment() {
        System.out.print("Enter department: ");
        String department = scanner.nextLine();
        
        List<Student> students = studentService.getStudentsByDepartment(department);
        System.out.println(StudentFormatter.createHeader("STUDENTS IN " + department.toUpperCase()));
        
        if (students.isEmpty()) {
            System.out.println("No students found in department: " + department);
        } else {
            System.out.println(StudentFormatter.formatStudentsAsTable(students));
            System.out.println("Average GPA for " + department + ": " + 
                    String.format("%.2f", studentService.calculateAverageGpaByDepartment(department)));
        }
    }

    private static void viewTopPerformingStudents() {
        System.out.println(StudentFormatter.createHeader("TOP PERFORMING STUDENTS"));
        List<Student> topStudents = studentService.getTopPerformingStudents();
        
        if (topStudents.isEmpty()) {
            System.out.println("No top performing students found.");
        } else {
            System.out.println("Students with GPA >= 3.5:");
            System.out.println(StudentFormatter.formatStudentsAsTable(topStudents));
        }
    }

    private static void viewStudentsAtRisk() {
        System.out.println(StudentFormatter.createHeader("STUDENTS AT RISK"));
        List<Student> atRiskStudents = studentService.getStudentsAtRisk();
        
        if (atRiskStudents.isEmpty()) {
            System.out.println("No students at risk found.");
        } else {
            System.out.println("Students with GPA < 2.0:");
            System.out.println(StudentFormatter.formatStudentsAsTable(atRiskStudents));
        }
    }

    private static void viewStatistics() {
        System.out.println(StudentFormatter.createHeader("SYSTEM STATISTICS"));
        System.out.println(studentService.generateStatisticsReport());
    }
}
