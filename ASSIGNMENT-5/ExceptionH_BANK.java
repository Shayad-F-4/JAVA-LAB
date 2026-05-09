import java.util.Scanner;

class InsufficientFundsException extends Exception {
    
    public InsufficientFundsException(String msg) {
        super(msg);
    }
}

class Account {
    int accNo;
    int password;
    String name;
    double balance;

    Account(int accNo, int password, String name, double balance){
        this.accNo = accNo;
        this.password = password;
        this.name = name;
        this.balance = balance;
    }

    void deposit(double amt) throws IllegalArgumentException, NullPointerException {
            if (amt <= 0) {
                throw new IllegalArgumentException("Amount should be in positive! ");
            }
        balance += amt;
    }

    void withdraw(double amt) throws InsufficientFundsException, IllegalArgumentException {// we can also use here IllegalArgumentException 
        if (amt <= 0){
            throw new IllegalArgumentException("Amount should be in positive! ");
        }
        if (amt > balance) {
            throw new InsufficientFundsException("Insufficient balance caught! ");
        }
        balance -= amt;
    }

    void show() {
        System.out.println("------------------------------");
        System.out.println("Name: "+ name);
        System.out.println("Account No: "+ accNo);
        System.out.println("Balance: "+ balance);
    }
}

public class  ExceptionH_BANK{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Account[] accounts = new Account[100];
        int count = 0;
        boolean run = true;

        while (run) {
            System.out.println("\n1.Create");
            System.out.println("2.Deposit");
            System.out.println("3.Withdraw");
            System.out.println("4.Show All");
            System.out.println("5.Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Name: ");
                    String name = sc.next();
                    System.out.print("Acc No: ");
                    int accNo = sc.nextInt();
                    System.out.print("Password: ");
                    int pass = sc.nextInt();
                    System.out.print("Initial Balance: ");
                    double bal = sc.nextDouble();
                    accounts[count++] = new Account(accNo, pass, name, bal);
                    System.out.println("Account Created");
                    break;

                case 2:
                    System.out.print("Acc No: ");
                    accNo = sc.nextInt();
                    System.out.print("Password: ");
                    pass = sc.nextInt();
                    boolean found = false;

                    for (int i = 0; i < count; i++){
                        if (accounts[i].accNo == accNo && accounts[i].password == pass){
                            try {
                                System.out.print("Amount: ");
                                accounts[i].deposit(sc.nextDouble());
                                System.out.println("Deposit Success");
                            } catch (IllegalArgumentException e){
                                System.out.println("Exception occured! ");
                                System.out.println(e.getMessage());
                            } finally {
                                System.out.println("Deposit Attempted");
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        System.out.println("Account not found"); 
                    break;

                case 3:
                    System.out.print("Acc No: ");
                    accNo = sc.nextInt();
                    System.out.print("Password: ");
                    pass = sc.nextInt();
                    found = false;

                    for (int i = 0; i < count; i++){
                        if (accounts[i].accNo == accNo && accounts[i].password == pass){
                            try {
                                System.out.print("Amount: ");
                                accounts[i].withdraw(sc.nextInt());
                                System.out.println("Withdraw Success");
                            }
                            catch(InsufficientFundsException | IllegalArgumentException e)
                            {
                                System.out.println("Exception occured! ");
                                System.out.println(e.getMessage());
                            }
                            finally
                            {
                                System.out.println("Withdraw Attempted");
                            }
                            found = true;
                            break;
                        }

                    }
                    if (!found)
                        System.out.println("Account not found");
                    break;

                case 4:
                    if (count == 0)
                        System.out.println("No Accounts");
                    else
                        for (int i = 0; i < count; i++)
                            accounts[i].show();
                    break;

                case 5:
                    run = false;
                    System.out.println("Exit");
                    break;

                default:
                    System.out.println("Invalid Choice");
            }
        }
        sc.close();
    }
}