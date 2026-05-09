import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class newappleteall extends Applet 
    implements ActionListener, KeyListener, FocusListener, ItemListener {

    //declare compo of ui
    Button clickBtn;
    TextField inputField;
    Checkbox toggleBox;
    Label displayLabel;
    String lastEvent = "Perform an action";

    public void init(){
        setLayout(new FlowLayout());

        displayLabel = new Label("Event Status: Waiting...");
        add(displayLabel);

        clickBtn = new Button("Submit");
        add(clickBtn);
        clickBtn.addActionListener(this);

// Initialize TextField (KeyListener & FocusListener): key listener for typing, focus listener for the focus events means when user clicks in or out of the text field
        inputField = new TextField("Type here......", 20);
        add(inputField);
        inputField.addKeyListener(this);    // Register Key
        inputField.addFocusListener(this);  // Register Focus

// Initialize Checkbox (ItemListener)
        toggleBox = new Checkbox("Enable Mode");
        add(toggleBox);
        toggleBox.addItemListener(this);    // item listener used for check box to detect when user clicks on it and change its state from checked to unchecked or vice versa
    }

//action listner
    public void actionPerformed(ActionEvent e) {
        lastEvent = "Button clicked!";
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        lastEvent = "Key Pressed: " + e.getKeyChar();
        repaint();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}


    public void focusGained(FocusEvent e) {
        inputField.setText(""); // Clear text when user clicks in
        lastEvent = "TextField focused!";
        repaint();
    }
    public void focusLost(FocusEvent e) {
        lastEvent = "TextField lost focus!";
        repaint();
    }

    public void itemStateChanged(ItemEvent e) {
        boolean state = (e.getStateChange() == ItemEvent.SELECTED);
        lastEvent = "Checkbox is now: " + (state ? "ON" : "OFF");
        repaint();
    }

    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawString("LATEST EVENT: " + lastEvent, 20, 150);
        g.drawString("Instructions: Click button, type in box, or toggle checkbox.", 20, 180);
    }
}



import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class MovieTicketApplet extends Applet implements ActionListener {

    Label l1, l2, l3, l4;
    TextField t1, t2, t3, t4;
    Button b1;

    public void init() {

        setLayout(new GridLayout(5,2));

        l1 = new Label("Number of Tickets:");
        l2 = new Label("Customer Name:");
        l3 = new Label("Price per Ticket:");
        l4 = new Label("Total Amount:");

        t1 = new TextField(10);
        t2 = new TextField(10);
        t3 = new TextField(10);
        t4 = new TextField(10);
        t4.setEditable(false);

        b1 = new Button("Calculate");

        add(l1); add(t1);
        add(l2); add(t2);
        add(l3); add(t3);
        add(l4); add(t4);
        add(b1);

        b1.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {

        try {
            int tickets = Integer.parseInt(t1.getText());
            String name = t2.getText();
            double price = Double.parseDouble(t3.getText());

            double total = tickets * price;

            t4.setText("Rs. " + total);

        } catch (Exception ex) {
            t4.setText("Invalid Input!");
        }
    }

    // Run as application
    public static void main(String[] args) {
        Frame f = new Frame("Movie Ticket Booking System");

        MovieTicketApplet app = new MovieTicketApplet();
        app.init();
        app.start();

        f.add(app);
        f.setSize(400,250);
        f.setVisible(true);
    }
}