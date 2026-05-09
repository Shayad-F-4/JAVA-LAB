// Optional - requires external library (JFreeChart)
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

class ChartUtil {
    static void showChart(Account acc) {
        JFrame chartFrame = new JFrame("Transaction Chart - " + acc.name);
        chartFrame.setSize(600, 400);
        chartFrame.setLayout(new BorderLayout());
        
        // Create a simple bar chart using JPanel
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g, acc);
            }
        };
        
        chartPanel.setPreferredSize(new Dimension(550, 300));
        chartFrame.add(chartPanel, BorderLayout.CENTER);
        
        // Add legend
        JPanel legendPanel = new JPanel();
        legendPanel.add(new JLabel("Green: Deposits | Red: Withdrawals"));
        chartFrame.add(legendPanel, BorderLayout.SOUTH);
        
        chartFrame.setVisible(true);
        chartFrame.setLocationRelativeTo(null);
    }
    
    private static void drawChart(Graphics g, Account acc) {
        if (acc.history.isEmpty()) {
            g.drawString("No transactions to display", 200, 150);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = 550;
        int height = 300;
        int margin = 50;
        int chartWidth = width - 2 * margin;
        int chartHeight = height - 2 * margin;
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(margin, height - margin, width - margin, height - margin); // X-axis
        g2d.drawLine(margin, margin, margin, height - margin); // Y-axis
        
        // Calculate max amount for scaling
        double maxAmount = 1000; // Default minimum
        for (Transaction t : acc.history) {
            if (t.amount > maxAmount) {
                maxAmount = t.amount;
            }
        }
        
        // Draw bars
        int barWidth = Math.max(5, chartWidth / Math.max(10, acc.history.size()));
        int spacing = 2;
        
        for (int i = 0; i < Math.min(acc.history.size(), 10); i++) {
            Transaction t = acc.history.get(i);
            int barHeight = (int) ((t.amount / maxAmount) * chartHeight);
            int x = margin + i * (barWidth + spacing);
            int y = height - margin - barHeight;
            
            // Set color based on transaction type
            if (t.type.equals("DEPOSIT")) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(Color.RED);
            }
            
            g2d.fillRect(x, y, barWidth, barHeight);
            
            // Draw amount label on top of bar
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 8));
            g2d.drawString(String.format("%.0f", t.amount), x, y - 2);
        }
        
        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Transaction History Chart", width/2 - 80, 20);
        
        // Draw axis labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString("Transactions", width/2 - 30, height - 10);
        g2d.drawString("Amount", 10, height/2);
    }
}