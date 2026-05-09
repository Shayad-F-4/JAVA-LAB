import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportPanel extends JPanel {

    JLabel total, today, top;
    JTable reportTable;
    DefaultTableModel reportModel;
    JScrollPane scrollPane;

    ReportPanel(){
        setLayout(new BorderLayout());

        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3));
        total = new JLabel("", JLabel.CENTER);
        today = new JLabel("", JLabel.CENTER);
        top = new JLabel("", JLabel.CENTER);
        total.setFont(new Font("Arial", Font.BOLD, 16));
        today.setFont(new Font("Arial", Font.BOLD, 16));
        top.setFont(new Font("Arial", Font.BOLD, 16));
        
        summaryPanel.add(total);
        summaryPanel.add(today);
        summaryPanel.add(top);

        add(summaryPanel, BorderLayout.NORTH);

        // Detailed Report Table
        String[] columns = {"Total Items", "Item Name", "Remaining", "Sales", "Price"};
        reportModel = new DefaultTableModel(columns, 0);
        reportTable = new JTable(reportModel);
        styleTable(reportTable);
        
        scrollPane = new JScrollPane(reportTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        add(scrollPane, BorderLayout.CENTER);

        // Bar Graph Panel (bottom)
        BarGraphPanel barGraph = new BarGraphPanel();
        add(barGraph, BorderLayout.SOUTH);

        loadData(barGraph);

        // Auto-refresh when tab is opened
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadData(barGraph);
            }
        });
    }

    private void styleTable(JTable table) {
        table.setFont(UITheme.MAIN_FONT);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(Color.WHITE); // No selection color effect
        table.setSelectionForeground(Color.BLACK);
        
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI()); // Removes native Windows hover effects
        header.setFont(UITheme.BOLD_FONT);
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);
    }

    void loadData(BarGraphPanel barGraph){
        try{
            Connection con = DBConnection.getConnection();
            
            // Clear existing data
            reportModel.setRowCount(0);

            // Check if bills table exists and load revenue
            double totalRevenue = 0.0;
            try {
                ResultSet rs1 = con.createStatement().executeQuery(
                    "SELECT SUM(total_amount) FROM bills");
                if(rs1.next()) totalRevenue = rs1.getDouble(1);
                total.setText("Total Revenue: ₹"+String.format("%.2f", totalRevenue));
            } catch (Exception e) {
                total.setText("Total Revenue: ₹0.00");
            }

            // Load today's revenue
            try {
                ResultSet rs2 = con.createStatement().executeQuery(
                    "SELECT SUM(total_amount) FROM bills WHERE DATE(date)=CURDATE()");
                if(rs2.next()) today.setText("Today's Revenue: ₹"+String.format("%.2f", rs2.getDouble(1)));
                else today.setText("Today's Revenue: ₹0.00");
            } catch (Exception e) {
                today.setText("Today's Revenue: ₹0.00");
            }

            // Load products and calculate simple report
            String productsQuery = "SELECT id, name, quantity, price FROM products ORDER BY name";
            ResultSet rsProducts = con.createStatement().executeQuery(productsQuery);
            
            int totalItems = 0;
            int totalSold = 0;
            int totalRemaining = 0;
            
            while(rsProducts.next()){
                String itemName = rsProducts.getString("name");
                int remaining = rsProducts.getInt("quantity");
                int sold = 0; // Default to 0 if no sales data
                double price = rsProducts.getDouble("price");
                
                // Try to get sales data if bill_items exists
                try {
                    // First check if bill_items table exists
                    ResultSet rsCheck = con.createStatement().executeQuery(
                        "SHOW TABLES LIKE 'bill_items'");
                    if (rsCheck.next()) {
                        // Table exists, get sales data
                        ResultSet rsSales = con.createStatement().executeQuery(
                            "SELECT COALESCE(SUM(quantity), 0) as sold FROM bill_items WHERE product_id = " + rsProducts.getInt("id"));
                        if(rsSales.next()) {
                            sold = rsSales.getInt("sold");
                        }
                        rsSales.close();
                    } else {
                        // Table doesn't exist, keep sold = 0
                        System.out.println("bill_items table not found");
                    }
                    rsCheck.close();
                } catch (Exception e) {
                    System.out.println("Error getting sales data: " + e.getMessage());
                    // Keep sold = 0
                }
                
                int total = sold + remaining;
                
                reportModel.addRow(new Object[]{
                    total,
                    itemName,
                    remaining,
                    sold,
                    "₹" + String.format("%.2f", price)
                });
                
                totalItems += total;
                totalSold += sold;
                totalRemaining += remaining;
            }
            
            // Add summary row
            reportModel.addRow(new Object[]{
                totalItems,
                "TOTAL",
                totalRemaining,
                totalSold,
                "₹" + String.format("%.2f", totalRevenue)
            });

            // Set top seller
            if(totalSold > 0) {
                top.setText("Total Items Sold: " + totalSold);
            } else {
                top.setText("No Sales Data Available");
            }

            // Load bar graph data
            try {
                ResultSet rs3 = con.createStatement().executeQuery(
                    "SELECT SUM(quantity) FROM products");
                if(rs3.next()) barGraph.remainingProducts = rs3.getInt(1);
            } catch (Exception e) {
                barGraph.remainingProducts = 0;
            }

            try {
                ResultSet rs4 = con.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM bills");
                if(rs4.next()) barGraph.totalSales = rs4.getInt(1);
            } catch (Exception e) {
                barGraph.totalSales = 0;
            }

        }catch(Exception e){e.printStackTrace();}
    }
}

class BarGraphPanel extends JPanel {
    int remainingProducts = 0;
    int totalSales = 0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        int max = Math.max(remainingProducts, totalSales);
        if (max == 0) max = 1; // Prevent division by zero

        int barWidth = 80;
        int spacing = 60;
        int startX = (width - (2 * barWidth + spacing)) / 2;
        int bottomY = height - 40;

        int greenHeight = (int) (((double) remainingProducts / max) * (height - 80));
        int redHeight = (int) (((double) totalSales / max) * (height - 80));

        // Draw Remaining Products (Green)
        g.setColor(new Color(46, 204, 113));
        g.fillRect(startX, bottomY - greenHeight, barWidth, greenHeight);
        g.setColor(Color.BLACK);
        g.drawString("Remaining: " + remainingProducts, startX, bottomY + 20);

        // Draw Sales (Red)
        g.setColor(new Color(231, 76, 60));
        g.fillRect(startX + barWidth + spacing, bottomY - redHeight, barWidth, redHeight);
        g.setColor(Color.BLACK);
        g.drawString("Sales: " + totalSales, startX + barWidth + spacing, bottomY + 20);
    }
}