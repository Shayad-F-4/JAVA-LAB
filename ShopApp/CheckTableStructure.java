import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class CheckTableStructure {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            DatabaseMetaData dbm = con.getMetaData();
            
            // Check products table structure
            System.out.println("=== PRODUCTS TABLE STRUCTURE ===");
            ResultSet rs = dbm.getColumns(null, null, "products", null);
            while (rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME") + " - " + rs.getString("TYPE_NAME"));
            }
            rs.close();
            
            // Check bills table structure
            System.out.println("\n=== BILLS TABLE STRUCTURE ===");
            rs = dbm.getColumns(null, null, "bills", null);
            while (rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME") + " - " + rs.getString("TYPE_NAME"));
            }
            rs.close();
            
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
