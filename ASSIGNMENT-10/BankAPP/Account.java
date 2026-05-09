import java.io.Serializable;
import java.util.*;

class Account implements Serializable {

    int accNo;
    String name;
    String password;
    double balance;

    ArrayList<Transaction> history = new ArrayList<>();

    Account(int accNo, String name, String password, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    synchronized void deposit(double amt) {
        if (amt <= 0) throw new IllegalArgumentException("Invalid amount");
        balance += amt;
        history.add(new Transaction("DEPOSIT", amt));
    }

    synchronized void withdraw(double amt) throws InsufficientFundsException {
        if (amt > balance)
            throw new InsufficientFundsException("Insufficient Balance!");
        balance -= amt;
        history.add(new Transaction("WITHDRAW", amt));
    }
}