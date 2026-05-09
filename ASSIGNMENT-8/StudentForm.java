import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import java.io.FileWriter;
import java.io.IOException;


public class StudentForm extends Applet implements ActionListener {

    Label title, l1, l2, l3, l4, l5;
    TextField name, roll, output;
    Choice course;
    Checkbox male, female;
    CheckboxGroup gender;
    Button submit, clear;

    public void init() {

        setLayout(new FlowLayout());

        //Styling 
        setBackground(Color.lightGray);

        //Title
        title = new Label("STUDENT REGISTRATION FORM", Label.CENTER);
        title.setForeground(Color.black);
        title.setFont(new Font("Scriptify", Font.BOLD, 20));

        l1 = new Label("Name:");
        l2 = new Label("Roll:");
        l3 = new Label("Course:");
        l4 = new Label("Gender:");
        l5 = new Label("Output:");

        name = new TextField(15);
        roll = new TextField(15);
        output = new TextField(35);

        course = new Choice();
        course.add("Computer");
        course.add("IT");
        course.add("Mechanical");

        gender = new CheckboxGroup();
        male = new Checkbox("Male", gender, true);
        female = new Checkbox("Female", gender, false);

        submit = new Button("Submit");
        clear = new Button("Clear");

        // Add components
        add(title);
        add(l1); add(name);
        add(l2); add(roll);
        add(l3); add(course);
        add(l4); add(male); add(female);
        add(l5); add(output);
        add(submit); add(clear);

        // Event handling
        submit.addActionListener(this);
        clear.addActionListener(this);
    }

    public void start() {
        System.out.println("Applet Started");
    }

    public void paint(Graphics g) {
        g.drawString("Fill the form and click Submit", 100, 60);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == submit) {

            String n = name.getText();
            String r = roll.getText();

            //Validation
            if (n.equals("") || r.equals("")) {
                output.setText("Error: Please fill all fields!");
                return;
            }

            String c = course.getSelectedItem();
            String g = gender.getSelectedCheckbox().getLabel();

            output.setText("DONE: " + n + " (" + r + ") | " + c + " | " + g);
            saveToJson(n, r, c, g);
        }
        if (e.getSource() == clear) {
            name.setText("");
            roll.setText("");
            output.setText("");
        }
    }



    public void saveToJson(String n, String r, String c, String g) {
    try {
        FileWriter writer = new FileWriter("student.json", true); // 'true' appends new data
        
        // Manually creating the JSON format string
        String jsonEntry = "{\n" +
                "  \"name\": \"" + n + "\",\n" +
                "  \"roll\": \"" + r + "\",\n" +
                "  \"course\": \"" + c + "\",\n" +
                "  \"gender\": \"" + g + "\"\n" +
                "},\n";

        writer.write(jsonEntry);
        writer.close();
        System.out.println("Data saved to student.json");
        
    } catch (IOException ex) {
        output.setText("File Error: " + ex.getMessage());
    }
}
    public static void main(String[] args){

        Frame f = new Frame("Student Registration Form");

        StudentForm app = new StudentForm();

        app.init();
        app.start();

        f.add(app);
        f.setSize(550, 350);//frame size
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter(){//window listener to close the application when the user clicks the close button on the window
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
}