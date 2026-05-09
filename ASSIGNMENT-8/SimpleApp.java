import java.awt.*;
import java.awt.event.*;

// Main application class implementing ActionListener for button clicks
public class SimpleApp extends Frame implements ActionListener {
    Button b;
    TextField tf;

    public SimpleApp() {
        // Set the window title and layout
        super("My Simple Program");
        setLayout(new FlowLayout());

        // Create a button and a text field
        b = new Button("Click Me");
        tf = new TextField("Enter Text Here", 20);

        // Register the listener to the button
        b.addActionListener(this);

        // Add components to the frame
        add(tf);
        add(b);

        // Set window size and make it visible
        setSize(300, 200);
        setVisible(true);
    }

    // Handle button click events
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b) {
            System.out.println("Button was pressed!");
            tf.setText("Action Triggered!");
        }
    }
    public static void main(String[] args) {
        
        new SimpleApp();
    }
}