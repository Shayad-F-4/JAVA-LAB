import java.sql.*;

public class DBConnection {
    
    public static void initializeDatabase() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/",
                "root",
                "shayad@123"
            );
            Statement stmt = conn.createStatement();
            
            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS new_shop_db");
            stmt.executeUpdate("USE new_shop_db");
            
            // Create products table if not exists
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS products (
                    id INT PRIMARY KEY,
                    name VARCHAR(100),
                    price DOUBLE,
                    quantity INT,
                    category VARCHAR(50)
                )
            """);
            

            
            // Create users table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE,
                    password VARCHAR(50),
                    role VARCHAR(20)
                )
            """);
            
            // Insert default users if table is empty
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if(rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate("INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin')");
                stmt.executeUpdate("INSERT INTO users (username, password, role) VALUES ('staff', 'staff123', 'staff')");
                System.out.println("✓ Default users created (admin/admin123, staff/staff123)");
            }
            rs.close();

            // Create customers table if not exists
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS customers (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    phone VARCHAR(20),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Create bills table if not exists
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS bills (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    customer_id INT,
                    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    total_amount DOUBLE,
                    discount DOUBLE DEFAULT 0.0,
                    gst DOUBLE DEFAULT 0.0,
                    FOREIGN KEY (customer_id) REFERENCES customers(id)
                )
            """);

            // Alter bills table to add discount and gst if they don't exist
            try {
                stmt.executeUpdate("ALTER TABLE bills ADD COLUMN discount DOUBLE DEFAULT 0.0");
            } catch (SQLException e) {
                // Ignore duplicate column error
            }
            try {
                stmt.executeUpdate("ALTER TABLE bills ADD COLUMN gst DOUBLE DEFAULT 0.0");
            } catch (SQLException e) {
                // Ignore duplicate column error
            }
            
            // Create bill_items table if not exists
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS bill_items (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    bill_id INT,
                    product_id INT,
                    quantity INT,
                    price DOUBLE,
                    discount DOUBLE DEFAULT 0.0,
                    FOREIGN KEY (bill_id) REFERENCES bills(id),
                    FOREIGN KEY (product_id) REFERENCES products(id)
                )
            """);
            
            System.out.println("✓ Billing tables created/verified");
            
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/new_shop_db",
                "root",
                "shayad@123" // change if needed
            );
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}