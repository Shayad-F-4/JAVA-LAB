import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Calculator extends JApplet implements ActionListener {
    private JTextField display;
    private JPanel panel;
    private String[] buttons = {
        "C", "^", "DEL", "/",
        "7", "8", "9", "*",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        " ", "0", ".", "="
    };
    private JButton[] btnArray = new JButton[20];
    private double num1 = 0, num2 = 0, result = 0;
    private char operator = '\0';
    private boolean startNewInput = true;
    private String currentInput = "";

    
    private Color bgColor = new Color(32, 32, 32);
    private Color btnColor = new Color(60, 60, 60);
    private Color operatorColor = new Color(255, 149, 0);
    private Color equalColor = new Color(28, 176, 246);
    private Color clearColor = new Color(230, 70, 70);
    private Color textColor = Color.WHITE;

    public void init() {
        setSize(320, 480);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.BOLD, 46));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(bgColor);
        display.setForeground(textColor);
        display.setBorder(new EmptyBorder(30, 15, 20, 15));
        add(display, BorderLayout.NORTH);

         
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 12, 12)); // 12px gaps btn the btns 
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(10, 15, 20, 15)); // Outer padding

        for (int i = 0; i < 20; i++) {
            if (buttons[i].equals(" ")) {
                panel.add(new JLabel(""));
                continue;
            }
            btnArray[i] = new JButton(buttons[i]);
            btnArray[i].setFont(new Font("Segoe UI", Font.BOLD, 24));
            btnArray[i].setFocusPainted(false);
            btnArray[i].setBorderPainted(false); 
            btnArray[i].setForeground(textColor);
            
            // Apply different colors based on buttons
            String btnText = buttons[i];
            if (btnText.matches("[0-9]") || btnText.equals(".")) {
                btnArray[i].setBackground(btnColor);
            } else if (btnText.equals("C") || btnText.equals("DEL")) {
                btnArray[i].setBackground(clearColor);
            } else if (btnText.equals("=")) {
                btnArray[i].setBackground(equalColor);
            } else {
                btnArray[i].setBackground(operatorColor);
            }

            btnArray[i].addActionListener(this);
            panel.add(btnArray[i]);
        }

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]") || command.equals(".")) {
            if (command.equals(".") && currentInput.contains(".")) {
                return; // Prevent multiple decimals
            }
            if (startNewInput) {
                if (command.equals(".")) {
                    command = "0.";
                }
                if (operator == '\0') {
                    display.setText(command);
                } else {
                    display.setText(display.getText() + command);
                }
                currentInput = command;
                startNewInput = false;
            } else {
                display.setText(display.getText() + command);
                currentInput += command;
            }
        } else if (command.equals("C")) {
            display.setText("0");
            num1 = num2 = result = 0;
            operator = '\0';
            currentInput = "";
            startNewInput = true;
        } else if (command.equals("DEL")) {
            if (!currentInput.isEmpty() && !startNewInput) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                String txt = display.getText();
                display.setText(txt.substring(0, txt.length() - 1));
                if (currentInput.isEmpty()) {
                    display.setText(txt.substring(0, txt.length() - 1) + "0");
                    currentInput = "";
                    startNewInput = true;
                }
            }
        } else if (command.equals("=")) {
            if (!startNewInput && operator != '\0' && !currentInput.isEmpty()) {
                num2 = Double.parseDouble(currentInput);
                calculateResult();
                display.setText(formatResult(result));
                num1 = result;
                operator = '\0';
                currentInput = "";
                startNewInput = true;
            }
        } else { // Operator buttons (+, -, *, /)
            if (!startNewInput) {
                if (operator != '\0' && !currentInput.isEmpty()) {
                    // Chain operations: 5 + 3 + ... -> 8 +
                    num2 = Double.parseDouble(currentInput);
                    calculateResult();
                    num1 = result;
                    display.setText(formatResult(result) + " " + command + " ");
                } else {
                    // First operator
                    num1 = Double.parseDouble(display.getText());
                    display.setText(display.getText() + " " + command + " ");
                }
            } else {
                // first number and have operator but not sencond operator then only change the operator
                if (operator != '\0') {
                    String txt = display.getText();
                    if (txt.length() >= 3) {
                        txt = txt.substring(0, txt.length() - 3) + " " + command + " ";
                        display.setText(txt);
                    }
                } else {
                    // After '=' or initial zero
                    num1 = Double.parseDouble(display.getText());
                    display.setText(display.getText() + " " + command + " ");
                }
            }
            operator = command.charAt(0);
            startNewInput = true;
            currentInput = "";
        }
    }

    private void calculateResult() {
        switch (operator) {
            case '+': result = num1 + num2; break;
            case '-': result = num1 - num2; break;
            case '*': result = num1 * num2; break;
            case '^': result = Math.pow(num1, num2); break;
            case '/': 
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    JOptionPane.showMessageDialog(this, "BRO U CAN'T DIVIDE BY ZERO!! ", "ERROR", JOptionPane.ERROR_MESSAGE);
                    result = 0;
                }
                break;
        }
    }

    //Prevents the decimal pt if the answer is a whole number
    private String formatResult(double res) {
        if (res == (long) res) {
            return String.format("%d", (long) res);
        } else {
            return String.valueOf(res);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        JFrame frame = new JFrame("Calculator");
        Calculator applet = new Calculator();
        
        frame.add(applet, BorderLayout.CENTER);
        frame.setSize(335, 580);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        applet.init();
        applet.start();
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
