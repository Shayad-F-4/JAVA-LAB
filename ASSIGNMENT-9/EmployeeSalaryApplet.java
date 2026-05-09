import java.applet.Applet;

import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;



public class EmployeeSalaryApplet extends Applet implements ActionListener {



    TextField idField, nameField, ageField;

    TextField basicField, hraField, bonusField, otherField;

    TextField totalField, avgField, perfField;



    Button calcBtn, saveBtn, deleteBtn, viewBtn, clearBtn;



    public void init() {



        setLayout(new BorderLayout());



        Panel form = new Panel(new GridLayout(10, 2, 10, 10));



        idField = addField(form, "ID:");

        nameField = addField(form, "Name:");

        ageField = addField(form, "Age:");

        basicField = addField(form, "Basic:");

        hraField = addField(form, "HRA:");

        bonusField = addField(form, "Bonus:");

        otherField = addField(form, "Other:");



        totalField = addField(form, "Total:");

        totalField.setEditable(false);



        avgField = addField(form, "Average:");

        avgField.setEditable(false);



        perfField = addField(form, "Performance:");

        perfField.setEditable(false);



        add(form, BorderLayout.CENTER);



        Panel btnPanel = new Panel();



        calcBtn = new Button("CALCULATE");

        saveBtn = new Button("SAVE");

        deleteBtn = new Button("DELETE");

        viewBtn = new Button("VIEW");

        clearBtn = new Button("CLEAR");



        btnPanel.add(calcBtn);

        btnPanel.add(saveBtn);

        btnPanel.add(deleteBtn);

        btnPanel.add(viewBtn);

        btnPanel.add(clearBtn);



        add(btnPanel, BorderLayout.SOUTH);



        calcBtn.addActionListener(this);

        saveBtn.addActionListener(this);

        deleteBtn.addActionListener(this);

        viewBtn.addActionListener(this);

        clearBtn.addActionListener(this);



        EmployeeDatabaseHelper.initializeDatabase();

    }



    private TextField addField(Panel panel, String label) {

        panel.add(new Label(label));

        TextField tf = new TextField();

        panel.add(tf);

        return tf;

    }



    public void actionPerformed(ActionEvent e) {



        if (e.getSource() == calcBtn) {

            try {

                double basic = Double.parseDouble(basicField.getText());

                double hra = Double.parseDouble(hraField.getText());

                double bonus = Double.parseDouble(bonusField.getText());

                double other = Double.parseDouble(otherField.getText());



                double total = basic + hra + bonus + other;

                double avg = total / 4;



                String perf = (avg > 50000) ? "Excellent"

                        : (avg > 30000) ? "Good"

                                : "Average";



                totalField.setText(String.valueOf(total));

                avgField.setText(String.valueOf(avg));

                perfField.setText(perf);



            } catch (Exception ex) {

                System.out.println("Invalid input!");

            }

        }



        if (e.getSource() == saveBtn) {

            boolean saved = EmployeeDatabaseHelper.saveEmployee(

                    idField.getText(),

                    nameField.getText(),

                    Integer.parseInt(ageField.getText()),

                    Double.parseDouble(basicField.getText()),

                    Double.parseDouble(hraField.getText()),

                    Double.parseDouble(bonusField.getText()),

                    Double.parseDouble(otherField.getText()),

                    Double.parseDouble(totalField.getText()),

                    Double.parseDouble(avgField.getText()),

                    perfField.getText());



            System.out.println(saved ? "Saved!" : "Error saving");

        }



        if (e.getSource() == deleteBtn) {

            boolean deleted = EmployeeDatabaseHelper.deleteEmployee(idField.getText());

            System.out.println(deleted ? "Deleted!" : "Not found");

System.out.println(deleted ? "Deleted!" : "Not found");

}


if (e.getSource() == clearBtn) {

idField.setText("");

nameField.setText("");

ageField.setText("");

basicField.setText("");

hraField.setText("");

bonusField.setText("");
            idField.setText("");

            nameField.setText("");

            ageField.setText("");

            basicField.setText("");

            hraField.setText("");

            bonusField.setText("");

            otherField.setText("");

            totalField.setText("");

            avgField.setText("");

            perfField.setText("");

        }

    }



    public static void main(String[] args) {

        Frame f = new Frame("Employee System");



        EmployeeSalaryApplet app = new EmployeeSalaryApplet();

        app.init();



        f.add(app);

        f.setSize(700, 500);

        f.setVisible(true);



        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {

                System.exit(0);

            }

        });

    }

}