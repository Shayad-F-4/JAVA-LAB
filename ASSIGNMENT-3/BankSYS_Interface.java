import java.util.*;

interface BankEntity {
    void display();
}

interface Transactionable extends BankEntity {
    void deposit(double amt);
    void withdraw(double amt);
}

interface Loanable extends BankEntity {
    double calculateEMI(double loanAmt, int months);
}

class Customer implements BankEntity {
    int accNo;
    String name;
    String bankName;

    Customer(int accNo, String name, String bankName) {
        this.accNo = accNo;
        this.name = name;
        this.bankName = bankName;
    }

    public void display() {
        System.out.println("Account Number: " + accNo);
        System.out.println("Customer Name: " + name);
        System.out.println("Bank Name: " + bankName);
    }
}

class Account implements Transactionable {
    int accNo;
    double balance;

    Account(int accNo, double balance) {
        this.accNo = accNo;
        this.balance = balance;
    }

    public void deposit(double amt) {
        if (amt > 0) {
            balance += amt;
            System.out.println("Deposited: " + amt + " | New Balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    public void withdraw(double amt) {
        if (amt <= balance && amt > 0) {
            balance -= amt;
            System.out.println("Withdrawn: " + amt + " | New Balance: " + balance);
        } else {
            System.out.println("Insufficient Balance or invalid amount!");
        }
    }

    public void display() {
        System.out.println("Account Number: " + accNo);
        System.out.println("Balance: " + balance);
    }
}

class Loan implements Loanable {
    int accNo;
    double annualRate = 8.5;

    Loan(int accNo) {
        this.accNo = accNo;
    }

    public double calculateEMI(double loanAmt, int months) {
        double monthlyRate = annualRate / 12 / 100;
        return (loanAmt * monthlyRate * Math.pow(1+monthlyRate, months)) /
               (Math.pow(1+monthlyRate, months)-1);
    }

    public void display() {
        System.out.println("Loan linked to Account: " + accNo);
        System.out.println("Annual Interest Rate: " + annualRate + "%");
    }
}

public class BankSYS_Interface {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Customer customer = null;
        Account account = null;
        Loan loan = null;

        int choice;
        do {
            System.out.println("\n--- BANK MENU ---");
            System.out.println("1. Create Customer");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Show Account");
            System.out.println("5. Loan EMI");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch(choice) {
                case 1:
                    System.out.print("Enter Account Number: ");
                    int accNo = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Bank Name: ");
                    String bankName = sc.nextLine();
                    System.out.print("Enter Initial Balance: ");
                    double bal = sc.nextDouble();

                    customer = new Customer(accNo, name, bankName);
                    account = new Account(accNo, bal);
                    loan = new Loan(accNo);

                    System.out.println("Customer & Account Created!");
                    break;

                case 2:
                    if (account != null) {
                        System.out.print("Enter Deposit Amount: ");
                        account.deposit(sc.nextDouble());
                    } else {
                        System.out.println("No account found!");
                    }
                    break;

                case 3:
                    if (account != null) {
                        System.out.print("Enter Withdraw Amount: ");
                        account.withdraw(sc.nextDouble());
                    } else {
                        System.out.println("No account found!");
                    }
                    break;

                case 4:
                    if (customer != null) customer.display();
                    if (account != null) account.display();
                    break;

                case 5:
                    if (loan != null) {
                        System.out.print("Enter Loan Amount: ");
                        double loanAmt = sc.nextDouble();
                        System.out.print("Enter Duration (months): ");
                        int months = sc.nextInt();
                        double emi = loan.calculateEMI(loanAmt, months);
                        loan.display();
                        System.out.println("Loan Amount: " + loanAmt);
                        System.out.println("Duration: " + months + " months");
                        System.out.printf("EMI: %.2f%n", emi);
                    } else {
                        System.out.println("No loan linked!");
                    }
                    break;

                case 6:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }
        } while(choice != 6);

        sc.close();
    }
}