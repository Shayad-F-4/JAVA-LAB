import java.sql.*;

public class SchemaPrinter {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection()) {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet rs = meta.getTables("shop_db", null, "%", new String[]{"TABLE"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
                ResultSet cols = meta.getColumns("shop_db", null, tableName, "%");
                while (cols.next()) {
                    System.out.println("  " + cols.getString("COLUMN_NAME") + " - " + cols.getString("TYPE_NAME"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
