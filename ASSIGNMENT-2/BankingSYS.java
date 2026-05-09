import java.util.Scanner;

abstract class Bank{
    String bankName;

    Bank(String bankName) {
        this.bankName = bankName;
    }

    abstract String getType();
}

class Customer extends Bank{
    String name;
    int accNo;
    int password;

    Customer(String bankName, String name, int accNo, int password) {
        super(bankName);
        this.name=name;
        this.accNo=accNo;
        this.password=password;
    }

    @Override
    String getType(){
        return "Account";
    }
}

class Account extends Bank{
    double balance;

    Account(String bankName, double balance) {
        super(bankName);
        this.balance=balance;
    }

    void deposit(double amt){
        this.balance+=amt;
    }

    void withdraw(double amt){
        if (amt<=this.balance)
            this.balance-=amt;
        else
            System.out.println("Insufficient balance");
    }

    @Override
    String getType() {
        return "Savings Account";
    }
}

final class Record{
    Customer c;
    Account a;

    Record(Customer c, Account a){
        this.c = c;
        this.a = a;
    }

    void show() {
        System.out.println("\n===== ACCOUNT DETAILS =====");
        System.out.println("Bank Name        : " + c.bankName);
        System.out.println("Account Holder   : " + c.name);
        System.out.println("Account Type     : " + a.getType());
        System.out.println("Account Number   : " + c.accNo);
        System.out.println("Account Balance  : " + a.balance);
        System.out.println("==========================");
    }
}

public class BankingSYS{
    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        Record records[] = new Record[100];
        int count = 0;                      

        int choice;
        int accNO;
        int pass;
        int serchNum;

        while (true){
            System.out.println("\n===Banking management System===");
            System.out.println("1.Create Account");
            System.out.println("2.Deposit");
            System.out.println("3.Withdraw");
            System.out.println("4.Show All Records");
            System.out.println("5.Search account");
            System.out.println("6.Update data");
            System.out.println("7.Loan.");
            System.out.println("8.Exit");
            System.out.println("\nEnter choice: ");
            choice = sc.nextInt();

            switch (choice){

                case 1:
                    System.out.println("Enter Bank Name: ");
                    String BankName = sc.next();

                    System.out.print("Enter Account Holder Name: ");
                    String name = sc.next();

                    System.out.print("Enter Account Number: ");
                    int accNo = sc.nextInt();

                    System.out.print("Enter Account Password: ");
                    int password = sc.nextInt();

                    System.out.print("Enter Initial Balance: ");
                    double bal = sc.nextDouble();

                    Customer c = new Customer(BankName, name, accNo, password);
                    Account a = new Account(BankName, bal);
                    Record r = new Record(c, a);

                    records[count] = r;
                    count++;

                    System.out.println("Account Created!");
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    serchNum = sc.nextInt();

                    System.out.print("Enter Account Password: ");
                    pass = sc.nextInt();

                    boolean found = false;

                    for (int i = 0; i < count; i++) {
                        if (records[i].c.accNo == serchNum && records[i].c.password == pass) {
                            System.out.print("Enter deposit amount: ");
                            records[i].a.deposit(sc.nextDouble());
                            System.out.println("Amount Deposited!");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Account not found or wrong password!");
                    }
                    break;

                case 3:
                    System.out.print("Enter Account Number: ");
                    serchNum = sc.nextInt();
                    System.out.println("Enter Account Password: ");
                    pass = sc.nextInt();
                    found = false;

                    for (int i = 0; i < count; i++) {
                        if (records[i].c.accNo == serchNum && records[i].c.password == pass) {
                            System.out.print("Enter withdraw amount: ");
                            records[i].a.withdraw(sc.nextDouble());
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Account not found!");
                    }
                    break;

                case 4:
                    if (count == 0) {
                        System.out.println("No record found!");
                    } else {
                        System.out.println("\n===== ALL CUSTOMERS DATA =====");
                        for (int i = 0; i < count; i++) {
                            records[i].show();
                        }
                    }
                    break;

                case 5:
                    System.out.println("Enter Account Number for search: ");
                    accNO = sc.nextInt();

                    found = false;

                    for (int i = 0; i < count; i++) {
                        if (records[i].c.accNo == accNO) {
                            System.out.println("Account Found!");
                            records[i].show();
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Account is Not found!");
                    }
                    break;

                case 6:
                    System.out.println("Enter Account Number: ");
                    accNO = sc.nextInt();

                    System.out.println("Enter Account Password: ");
                    pass = sc.nextInt();

                    found = false;

                    for (int i = 0; i < count; i++) {
                        if (records[i].c.accNo == accNO && records[i].c.password == pass) {

                            System.out.println("Enter new Account Holder Name: ");
                            String newname = sc.next();

                            System.out.println("Enter new Password: ");
                            int newpass = sc.nextInt();

                            records[i].c.name = newname;
                            records[i].c.password = newpass;

                            System.out.println("Account Data Updated!");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Account not found or incorrect password!");
                    }
                    break;

                case 7:   // LOAN
                    System.out.println("Enter the bank account number: ");
                    accNO = sc.nextInt();

                    found = false;

                    for (int i = 0; i < count; i++) {
                        if (records[i].c.accNo == accNO) {

                            System.out.println("Enter Loan Amount: ");
                            int loanamt = sc.nextInt();

                            System.out.println("Enter Duration (in month): ");
                            int months = sc.nextInt();

                            double annualRate = 8.5;
                            double monthlyRate = annualRate / 12 / 100;

                            double emi = (loanamt * monthlyRate * Math.pow(1+monthlyRate, months)) /
                                         (Math.pow(1 + monthlyRate, months)-1);

                            System.out.println("Amount of Loan "+loanamt);
                            System.out.println("Annual interest Fix "+annualRate +"%");
                            System.out.println("Duration "+months+" Months");
                            System.out.println("EMI "+emi);

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Account number not found!");
                    }
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
