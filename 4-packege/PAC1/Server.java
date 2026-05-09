package PAC1;

import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    private ArrayList<Database> users = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public void register() {

        System.out.println("----- Register -----");

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        System.out.print("Enter Role (Admin/User): ");
        String role = sc.nextLine();

        // Check if email already exists
        for (Database user : users) {
            if (user.getEmail().equals(email)) {
                System.out.println("\nEmail already registered!");
                return;
            }
        }

        Database newUser = new Database(name, age, email, password, role);
        users.add(newUser);

        System.out.println("\nRegistration Successful!\n");
    }

    public void login() {

        System.out.println("----- Login -----");

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        for (Database user : users) {
            if (user.getEmail().equals(email)) {

                if (user.getPassword().equals(password)) {
                    System.out.println("\nLogin Successful!");
                    System.out.println("Welcome " + user.getName());
                    System.out.println("Role: " + user.getRole());

                    user.accessingSchemaInDatabase();

                    return;
                } else {
                    System.out.println("\nWrong Password!");
                    return;
                }
            }
        }

        System.out.println("\nUser Not Found!");
    }
}
