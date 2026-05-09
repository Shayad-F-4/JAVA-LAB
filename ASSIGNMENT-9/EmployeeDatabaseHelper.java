import java.sql.*;

public class EmployeeDatabaseHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "shayad@123";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver Loaded");
        }catch(Exception e){
            System.out.println("Driver not found");
        }
    }

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS employee_db");
            stmt.executeUpdate("USE employee_db");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS employees (
                    emp_id VARCHAR(20) PRIMARY KEY,
                    name VARCHAR(100),
                    age INT,
                    basic DOUBLE,
                    hra DOUBLE,
                    bonus DOUBLE,
                    other DOUBLE,
                    total DOUBLE,
                    average DOUBLE,
                    performance VARCHAR(50)
                )
            """);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static boolean saveEmployee(String id, String name, int age,
                                       double basic, double hra, double bonus, double other,
                                       double total, double avg, String performance) {

        String sql = """
            INSERT INTO employees 
            (emp_id, name, age, basic, hra, bonus, other, total, average, performance)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            name=?, age=?, basic=?, hra=?, bonus=?, other=?, total=?, average=?, performance=?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setDouble(4, basic);
            ps.setDouble(5, hra);
            ps.setDouble(6, bonus);
            ps.setDouble(7, other);
            ps.setDouble(8, total);
            ps.setDouble(9, avg);
            ps.setString(10, performance);

            ps.setString(11, name);
            ps.setInt(12, age);
            ps.setDouble(13, basic);
            ps.setDouble(14, hra);
            ps.setDouble(15, bonus);
            ps.setDouble(16, other);
            ps.setDouble(17, total);
            ps.setDouble(18, avg);
            ps.setString(19, performance);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteEmployee(String id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE emp_id=?")) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String viewAllEmployees() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM employees ORDER BY emp_id")) {

            while (rs.next()) {
                sb.append(rs.getString("emp_id")).append("\t");
                sb.append(rs.getString("name")).append("\t");
                sb.append(rs.getInt("age")).append("\t");
                sb.append(rs.getDouble("basic")).append("\t");
                sb.append(rs.getDouble("hra")).append("\t");
                sb.append(rs.getDouble("bonus")).append("\t");
                sb.append(rs.getDouble("other")).append("\t");
                sb.append(rs.getDouble("total")).append("\t");
                sb.append(rs.getDouble("average")).append("\t");
                sb.append(rs.getString("performance")).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving data: " + e.getMessage();
        }
        return sb.toString();
    }
}