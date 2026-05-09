import java.sql.Connection;
import java.sql.Statement;

public class DropAndCreateBillItems {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            
            // Drop table if exists
            try {
                stmt.executeUpdate("DROP TABLE IF EXISTS bill_items");
                System.out.println("Dropped existing bill_items table");
            } catch (Exception e) {
                System.out.println("Table didn't exist or couldn't be dropped");
            }
            
            // Create new table
            String sql = "CREATE TABLE bill_items (" +
                         "bill_item_id INT AUTO_INCREMENT PRIMARY KEY," +
                         "bill_id INT NOT NULL," +
                         "product_id VARCHAR(50) NOT NULL," +
                         "quantity INT NOT NULL," +
                         "price DECIMAL(10, 2) NOT NULL," +
                         "discount DECIMAL(10, 2) DEFAULT 0.00" +
                         ")";
            
            stmt.executeUpdate(sql);
            System.out.println("bill_items table created successfully!");
            
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
