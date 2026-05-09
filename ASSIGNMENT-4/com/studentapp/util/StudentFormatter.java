package com.studentapp.util;

import com.studentapp.model.Student;
import java.util.List;

/**
 * Utility class for formatting student data for display
 * This demonstrates formatting utilities and display formatting
 */
public class StudentFormatter {

    /**
     * Format student information for display
     */
    public static String formatStudent(Student student) {
        if (student == null) {
            return "No student data available";
        }

        StringBuilder formatted = new StringBuilder();
        formatted.append("Student ID: ").append(student.getStudentId()).append("\n");
        formatted.append("Name: ").append(student.getFullName()).append("\n");
        formatted.append("Email: ").append(student.getEmail()).append("\n");
        formatted.append("Phone: ").append(student.getPhoneNumber()).append("\n");
        formatted.append("Department: ").append(student.getDepartment()).append("\n");
        formatted.append("GPA: ").append(String.format("%.2f", student.getGpa())).append("\n");
        formatted.append("Grade Level: ").append(student.getGradeLevel()).append("\n");
        formatted.append("Age: ").append(student.getAge()).append(" years old\n");

        return formatted.toString();
    }

    /**
     * Format student as table row
     */
    public static String formatStudentAsTableRow(Student student) {
        return String.format("| %-6d | %-20s | %-25s | %-15s | %-6.2f | %-12s |",
                student.getStudentId(),
                student.getFullName(),
                student.getEmail(),
                student.getDepartment(),
                student.getGpa(),
                student.getGradeLevel());
    }

    /**
     * Format list of students as table
     */
    public static String formatStudentsAsTable(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return "No students to display";
        }

        StringBuilder table = new StringBuilder();
        
        // Table header
        table.append("+--------+----------------------+---------------------------+-----------------+--------+--------------+\n");
        table.append("| ID     | Name                 | Email                     | Department      | GPA    | Grade Level  |\n");
        table.append("+--------+----------------------+---------------------------+-----------------+--------+--------------+\n");

        // Table rows
        for (Student student : students) {
            table.append(formatStudentAsTableRow(student)).append("\n");
        }

        // Table footer
        table.append("+--------+----------------------+---------------------------+-----------------+--------+--------------+\n");

        return table.toString();
    }

    /**
     * Format student summary (one line)
     */
    public static String formatStudentSummary(Student student) {
        return String.format("ID: %d, Name: %s, Dept: %s, GPA: %.2f",
                student.getStudentId(),
                student.getFullName(),
                student.getDepartment(),
                student.getGpa());
    }

    /**
     * Format department statistics
     */
    public static String formatDepartmentStatistics(String department, long count, double avgGpa) {
        return String.format("Department: %-15s | Students: %3d | Avg GPA: %.2f",
                department, count, avgGpa);
    }

    /**
     * Create a separator line
     */
    public static String createSeparator(int length) {
        return "=".repeat(length);
    }

    /**
     * Create a header with title
     */
    public static String createHeader(String title) {
        StringBuilder header = new StringBuilder();
        int titleLength = title.length();
        int totalLength = Math.max(titleLength + 10, 60);
        
        String separator = createSeparator(totalLength);
        
        header.append(separator).append("\n");
        header.append(" ".repeat((totalLength - titleLength) / 2))
              .append(title)
              .append("\n");
        header.append(separator).append("\n");
        
        return header.toString();
    }

    /**
     * Format student report with all details
     */
    public static String formatStudentReport(Student student) {
        StringBuilder report = new StringBuilder();
        
        report.append(createHeader("STUDENT DETAILS"));
        report.append(formatStudent(student));
        
        // Academic performance section
        report.append("\n").append(createHeader("ACADEMIC PERFORMANCE"));
        report.append("Current GPA: ").append(String.format("%.2f", student.getGpa())).append("/4.0\n");
        report.append("Grade Level: ").append(student.getGradeLevel()).append("\n");
        
        if (student.getGpa() >= 3.5) {
            report.append("Status: Top Performer ⭐\n");
        } else if (student.getGpa() < 2.0) {
            report.append("Status: At Risk ⚠️\n");
        } else {
            report.append("Status: Satisfactory ✓\n");
        }
        
        // Contact information section
        report.append("\n").append(createHeader("CONTACT INFORMATION"));
        report.append("Primary Email: ").append(student.getEmail()).append("\n");
        report.append("Phone Number: ").append(student.getPhoneNumber()).append("\n");
        
        return report.toString();
    }

    /**
     * Format multiple students for batch display
     */
    public static String formatStudentBatch(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return "No students to display";
        }

        StringBuilder batch = new StringBuilder();
        batch.append(createHeader("STUDENT BATCH DISPLAY"));
        batch.append("Total Students: ").append(students.size()).append("\n\n");
        
        for (int i = 0; i < students.size(); i++) {
            batch.append("Student ").append(i + 1).append(":\n");
            batch.append(formatStudentSummary(students.get(i))).append("\n");
            
            if (i < students.size() - 1) {
                batch.append("-".repeat(50)).append("\n");
            }
        }
        
        return batch.toString();
    }
}
