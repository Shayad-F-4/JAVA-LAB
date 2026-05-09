package com.studentapp.service;

import com.studentapp.model.Student;
import com.studentapp.util.StudentValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * StudentService class handles all business logic for student management
 * This demonstrates service layer architecture and business operations
 */
public class StudentService {
    private List<Student> students;
    private int nextStudentId;

    public StudentService() {
        this.students = new ArrayList<>();
        this.nextStudentId = 1001;
        initializeSampleData();
    }

    /**
     * Add a new student to the system
     */
    public Student addStudent(Student student) {
        if (!StudentValidator.isValidStudent(student)) {
            throw new IllegalArgumentException("Invalid student data");
        }
        
        student.setStudentId(nextStudentId++);
        students.add(student);
        return student;
    }

    /**
     * Add a new student with individual parameters
     */
    public Student addStudent(String firstName, String lastName, String email, 
                             String phoneNumber, String department, double gpa) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setDepartment(department);
        student.setGpa(gpa);
        
        return addStudent(student);
    }

    /**
     * Get student by ID
     */
    public Optional<Student> getStudentById(int studentId) {
        return students.stream()
                .filter(student -> student.getStudentId() == studentId)
                .findFirst();
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Update student information
     */
    public boolean updateStudent(int studentId, Student updatedStudent) {
        if (!StudentValidator.isValidStudent(updatedStudent)) {
            throw new IllegalArgumentException("Invalid student data");
        }

        Optional<Student> existingStudent = getStudentById(studentId);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setFirstName(updatedStudent.getFirstName());
            student.setLastName(updatedStudent.getLastName());
            student.setEmail(updatedStudent.getEmail());
            student.setPhoneNumber(updatedStudent.getPhoneNumber());
            student.setDepartment(updatedStudent.getDepartment());
            student.setGpa(updatedStudent.getGpa());
            return true;
        }
        return false;
    }

    /**
     * Delete a student by ID
     */
    public boolean deleteStudent(int studentId) {
        return students.removeIf(student -> student.getStudentId() == studentId);
    }

    /**
     * Search students by name
     */
    public List<Student> searchStudentsByName(String name) {
        String searchTerm = name.toLowerCase();
        return students.stream()
                .filter(student -> 
                    student.getFirstName().toLowerCase().contains(searchTerm) ||
                    student.getLastName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Get students by department
     */
    public List<Student> getStudentsByDepartment(String department) {
        return students.stream()
                .filter(student -> student.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    /**
     * Get students with GPA above threshold
     */
    public List<Student> getStudentsWithGpaAbove(double threshold) {
        return students.stream()
                .filter(student -> student.getGpa() >= threshold)
                .collect(Collectors.toList());
    }

    /**
     * Get top performing students (GPA >= 3.5)
     */
    public List<Student> getTopPerformingStudents() {
        return getStudentsWithGpaAbove(3.5);
    }

    /**
     * Get students at risk (GPA < 2.0)
     */
    public List<Student> getStudentsAtRisk() {
        return students.stream()
                .filter(student -> student.getGpa() < 2.0)
                .collect(Collectors.toList());
    }

    /**
     * Calculate average GPA for all students
     */
    public double calculateAverageGpa() {
        return students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
    }

    /**
     * Calculate average GPA by department
     */
    public double calculateAverageGpaByDepartment(String department) {
        return students.stream()
                .filter(student -> student.getDepartment().equalsIgnoreCase(department))
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
    }

    /**
     * Get student count by department
     */
    public long getStudentCountByDepartment(String department) {
        return students.stream()
                .filter(student -> student.getDepartment().equalsIgnoreCase(department))
                .count();
    }

    /**
     * Get total number of students
     */
    public int getTotalStudentCount() {
        return students.size();
    }

    /**
     * Clear all students
     */
    public void clearAllStudents() {
        students.clear();
        nextStudentId = 1001;
    }

    /**
     * Initialize with sample data for demonstration
     */
    private void initializeSampleData() {
        addStudent("John", "Doe", "john.doe@university.edu", "555-0101", "Computer Science", 3.8);
        addStudent("Jane", "Smith", "jane.smith@university.edu", "555-0102", "Mathematics", 3.9);
        addStudent("Mike", "Johnson", "mike.johnson@university.edu", "555-0103", "Physics", 3.2);
        addStudent("Sarah", "Williams", "sarah.williams@university.edu", "555-0104", "Computer Science", 3.7);
        addStudent("David", "Brown", "david.brown@university.edu", "555-0105", "Mathematics", 2.8);
        addStudent("Emily", "Davis", "emily.davis@university.edu", "555-0106", "Physics", 1.9);
    }

    /**
     * Generate student statistics report
     */
    public String generateStatisticsReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== STUDENT MANAGEMENT SYSTEM STATISTICS ===\n\n");
        
        report.append("Total Students: ").append(getTotalStudentCount()).append("\n\n");
        
        report.append("Students by Department:\n");
        students.stream()
                .collect(Collectors.groupingBy(Student::getDepartment, Collectors.counting()))
                .forEach((dept, count) -> 
                    report.append("  ").append(dept).append(": ").append(count).append("\n"));
        
        report.append("\nAverage GPA: ").append(String.format("%.2f", calculateAverageGpa())).append("\n\n");
        
        report.append("Top Performing Students (GPA >= 3.5): ").append(getTopPerformingStudents().size()).append("\n");
        report.append("Students at Risk (GPA < 2.0): ").append(getStudentsAtRisk().size()).append("\n");
        
        return report.toString();
    }
}
