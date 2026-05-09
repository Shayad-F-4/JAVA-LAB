import java.util.Scanner;

class BankRecord {
    String name;
    int accNo;
    double balance;

    BankRecord(Customer c, Account a){
        name = c.name;
        accNo = c.accNo;
        balance = a.balance;
    }
}

class Customer{
    String name;
    int accNo;

    void inputCustomer(Scanner sc){
        System.out.print("Enter Customer Name: ");
        name = sc.next();

        System.out.print("Enter Account Number: ");
        accNo = sc.nextInt();
    }
}

class Account{
    double balance;

    void inputBalance(Scanner sc){
        System.out.print("Enter Initial Balance: ");
        balance = sc.nextDouble();
    }

    void deposit(double amt){
        if(amt < 0){
            System.out.println("Amount should not be negative!");
        }
        else{
            balance = balance + amt;
        }
    }

    void withdraw(double amt){
        if(amt <= balance){
            balance = balance - amt;
        }
        else{
            System.out.println("Insufficient Balance!");
        }
    }
}

class Transaction{
    void doTransaction(Account a, Scanner sc){
        System.out.print("Enter amount to deposit: ");
        double d = sc.nextDouble();
        a.deposit(d);

        System.out.print("Enter amount to withdraw: ");
        double w = sc.nextDouble();
        a.withdraw(w);
    }
}

class Display{
    void displaying(Customer c, Account a){
        System.out.println("=== SBI BANK ===");
        System.out.println("Name: " + c.name);
        System.out.println("Account No: " + c.accNo);
        System.out.println("Balance: " + a.balance);
    }
}
class BankSYS{
    public static void main(String args[]){

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of customers: ");
        int n = sc.nextInt();

        Customer[] c = new Customer[n];
        Account[] a = new Account[n];

        Transaction t = new Transaction();
        Display d = new Display();

        for(int i = 0; i < n; i++){
            System.out.println("\nEnter details of Customer " + (i+1));

            c[i] = new Customer();
            a[i] = new Account();

            c[i].inputCustomer(sc);
            a[i].inputBalance(sc);

            t.doTransaction(a[i], sc);
        }

        for(int i = 0; i < n; i++){
            System.out.println("\nCustomer Details " + (i+1));
            d.displaying(c[i], a[i]);
        }
    }
}