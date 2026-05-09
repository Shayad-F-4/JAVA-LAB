import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {

    private CardLayout cl = new CardLayout();
    private JPanel mainPanel = new JPanel(cl);
    private String userRole;

    public Dashboard(String role) {
        this.userRole = role;
        setTitle("Shop App Management - " + (role.equalsIgnoreCase("admin") ? "Administrator" : "Staff"));
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 10, 10));
        sidebar.setBackground(UITheme.SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Add Header to sidebar
        JLabel lblHeader = new JLabel("SHOP APP", SwingConstants.CENTER);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        sidebar.add(lblHeader);

        // Sidebar buttons
        JButton btnProducts = createSidebarButton("Products");
        JButton btnCustomers = createSidebarButton("Customers");
        JButton btnBilling = createSidebarButton("Billing");
        JButton btnReport = createSidebarButton("Detailed Report");
        JButton btnSales = createSidebarButton("Sales Dashboard");
        JButton btnLogout = createSidebarButton("Logout");

        // Add to Sidebar
        sidebar.add(btnProducts);
        sidebar.add(btnCustomers);
        sidebar.add(btnBilling);
        
        // Add all panels to main panel
        mainPanel.add(new ProductPanel(), "Products");
        mainPanel.add(new CustomerPanel(), "Customers");
        mainPanel.add(new BillingPanel(), "Billing");
        
        // Admin only panels
        if (role.equalsIgnoreCase("admin")) {
            sidebar.add(btnReport);
            sidebar.add(btnSales);
            mainPanel.add(new ReportPanel(), "Report");
            mainPanel.add(new SalesReportPanel(), "Sales");
        }

        // Add logout button at the end
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setOpaque(false);
        logoutPanel.add(btnLogout, BorderLayout.SOUTH);
        sidebar.add(logoutPanel);

        // Action Listeners
        btnProducts.addActionListener(e -> cl.show(mainPanel, "Products"));
        btnCustomers.addActionListener(e -> cl.show(mainPanel, "Customers"));
        btnBilling.addActionListener(e -> cl.show(mainPanel, "Billing"));
        
        if (role.equalsIgnoreCase("admin")) {
            btnReport.addActionListener(e -> cl.show(mainPanel, "Report"));
            btnSales.addActionListener(e -> cl.show(mainPanel, "Sales"));
        }
        
        btnLogout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        // Add components to Frame
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(UITheme.PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }
}