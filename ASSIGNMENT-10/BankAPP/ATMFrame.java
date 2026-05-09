import javax.swing.*;

class ATMFrame extends JFrame {

    ATMFrame(Account acc) {

        setTitle("ATM");
        setSize(300,300);
        setLayout(new java.awt.GridLayout(3,1));

        JButton withdraw = new JButton("Withdraw");
        JButton deposit = new JButton("Deposit");
        JButton balance = new JButton("Balance");

        add(withdraw); add(deposit); add(balance);

        withdraw.addActionListener(e -> {
            double amt = Double.parseDouble(
                JOptionPane.showInputDialog("Amount"));
            try {
                acc.withdraw(amt);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        deposit.addActionListener(e -> {
            double amt = Double.parseDouble(
                JOptionPane.showInputDialog("Amount"));
            acc.deposit(amt);
        });

        balance.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Balance: ₹"+acc.balance);
        });

        setVisible(true);
    }
}