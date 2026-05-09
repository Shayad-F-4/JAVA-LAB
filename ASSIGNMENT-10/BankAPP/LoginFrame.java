import javax.swing.*;
import java.awt.*;

class LoginFrame extends JFrame {

    JTextField accField;
    JPasswordField passField;
    JButton loginBtn, registerBtn;

    BankService service = new BankService();

    LoginFrame() {
        setTitle("Bank Login");
        setSize(300,200);
        setLayout(new GridLayout(4,2));

        accField = new JTextField();
        passField = new JPasswordField();

        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");

        add(new JLabel("Account No")); add(accField);
        add(new JLabel("Password")); add(passField);
        add(loginBtn); add(registerBtn);

        loginBtn.addActionListener(e -> {
            Account a = service.login(
                Integer.parseInt(accField.getText()),
                new String(passField.getPassword())
            );

            if (a != null) {
                new Dashboard(a, service);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login");
            }
        });

        registerBtn.addActionListener(e -> {
            int acc = Integer.parseInt(accField.getText());
            String pass = new String(passField.getPassword());

            service.create(acc, "User"+acc, pass, 1000);
            JOptionPane.showMessageDialog(this, "Account Created!");
        });

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}