import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    JTextField user;
    JPasswordField pass;

    LoginFrame(){
        setTitle("Shop App Login");
        setSize(400,300);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_COLOR);
        
        // Header
        JLabel headerLabel = new JLabel("Welcome to Shop App", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(UITheme.PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(UITheme.MAIN_FONT);
        user = new JTextField(15);
        user.setFont(UITheme.MAIN_FONT);
        user.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UITheme.MAIN_FONT);
        pass = new JPasswordField(15);
        pass.setFont(UITheme.MAIN_FONT);
        pass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(user, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(pass, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(UITheme.BG_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        loginBtn.setFont(UITheme.BOLD_FONT);
        loginBtn.setBackground(UITheme.PRIMARY_COLOR);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(150, 35));
        
        buttonPanel.add(loginBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loginBtn.addActionListener(e -> login());
        
        // Add Enter key support
        pass.addActionListener(e -> login());
        user.addActionListener(e -> pass.requestFocus());

        setVisible(true);
    }

    void login(){
        try{
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT role FROM users WHERE username=? AND password=?");

            ps.setString(1,user.getText());
            ps.setString(2,new String(pass.getPassword()));

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                new Dashboard(rs.getString("role"));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
            
            rs.close();
            ps.close();
            con.close();

        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}