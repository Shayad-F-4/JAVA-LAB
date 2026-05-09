import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SalesReportPanel extends JPanel {

    double total = 0, today = 0;
    int count = 0;

    public SalesReportPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_COLOR);

        JLabel title = new JLabel("Sales Dashboard", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(UITheme.PRIMARY_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(title, BorderLayout.NORTH);

        loadData();

        new Timer(5000, e -> {
            loadData();
            repaint();
        }).start();
    }

    void loadData() {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return;
            Statement st = con.createStatement();

            ResultSet rs1 = st.executeQuery("SELECT SUM(total_amount) FROM bills");
            if (rs1.next()) total = rs1.getDouble(1);

            ResultSet rs2 = st.executeQuery(
                "SELECT SUM(total_amount) FROM bills WHERE DATE(date)=CURDATE()");
            if (rs2.next()) today = rs2.getDouble(1);

            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) FROM bills");
            if (rs3.next()) count = rs3.getInt(1);

            rs1.close();
            rs2.close();
            rs3.close();
            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println("No data yet or database error: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw Stats Box
        int startX = 50;
        int startY = 80;
        int boxWidth = 250;
        int boxHeight = 150;
        
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(startX, startY, boxWidth, boxHeight, 15, 15);
        g2d.setColor(new Color(220, 220, 220));
        g2d.drawRoundRect(startX, startY, boxWidth, boxHeight, 15, 15);

        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Summary Stats", startX + 20, startY + 30);

        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.drawString("Total Sales: ₹" + String.format("%.2f", total), startX + 20, startY + 70);
        g2d.drawString("Today's Sales: ₹" + String.format("%.2f", today), startX + 20, startY + 100);
        g2d.drawString("Total Bills Generated: " + count, startX + 20, startY + 130);

        // Draw Pie Chart
        int pieX = 350;
        int pieY = 60;
        int pieSize = 200;

        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Sales Distribution", pieX + 25, pieY - 15);

        int angle = total == 0 ? 0 : (int) ((today / total) * 360);

        // Today's Sales Arc
        g2d.setColor(new Color(46, 204, 113)); // Green
        g2d.fillArc(pieX, pieY, pieSize, pieSize, 0, angle);

        // Other Sales Arc
        g2d.setColor(new Color(52, 152, 219)); // Blue
        g2d.fillArc(pieX, pieY, pieSize, pieSize, angle, 360 - angle);

        // Legend
        int legendX = pieX + pieSize + 50;
        int legendY = pieY + 50;

        g2d.setColor(new Color(46, 204, 113));
        g2d.fillRect(legendX, legendY, 20, 20);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Today's Sales", legendX + 30, legendY + 15);

        g2d.setColor(new Color(52, 152, 219));
        g2d.fillRect(legendX, legendY + 40, 20, 20);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("Other Sales", legendX + 30, legendY + 55);
        
        // Empty State
        if(total == 0) {
            g2d.setColor(Color.WHITE);
            g2d.fillArc(pieX, pieY, pieSize, pieSize, 0, 360);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawArc(pieX, pieY, pieSize, pieSize, 0, 360);
            g2d.setColor(Color.GRAY);
            g2d.drawString("No Data", pieX + 70, pieY + 105);
        }
    }
}