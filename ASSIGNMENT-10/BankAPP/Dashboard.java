import javax.swing.*;
import java.awt.*;

class Dashboard extends JFrame {

    Account acc;
    JTextArea area;

    Dashboard(Account acc, BankService service) {
        this.acc = acc;

        setTitle("Dashboard");
        setSize(400,400);
        setLayout(new FlowLayout());

        Theme.apply(this);

        JButton dep = Theme.darkButton("Deposit");
        JButton wit = Theme.darkButton("Withdraw");
        JButton view = Theme.darkButton("View");
        JButton history = Theme.darkButton("History");
        JButton chart = Theme.darkButton("Chart");
        JButton atm = Theme.darkButton("ATM");

        area = new JTextArea(10,30);

        add(dep); add(wit); add(view);
        add(history); add(chart);
        add(atm);
        add(area);

        dep.addActionListener(e -> {
            double amt = Double.parseDouble(
                JOptionPane.showInputDialog("Amount"));
            new TransactionThread(acc, amt, true).start();
        });

        wit.addActionListener(e -> {
            double amt = Double.parseDouble(
                JOptionPane.showInputDialog("Amount"));
            new TransactionThread(acc, amt, false).start();
        });

        view.addActionListener(e -> {
            area.setText("Name: "+acc.name+"\nBalance: "+acc.balance);
        });

        history.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Transaction t : acc.history) {
                sb.append(t).append("\n");
            }
            area.setText(sb.toString());
        });

        chart.addActionListener(e -> ChartUtil.showChart(acc));

        atm.addActionListener(e -> new ATMFrame(acc));

        setVisible(true);
    }
}