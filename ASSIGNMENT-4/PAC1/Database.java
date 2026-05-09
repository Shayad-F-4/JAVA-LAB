package PAC1;

import java.util.*;

public class Database extends Schema {

    public Database(String name, int age, String email, String password, String role) {
        super(name, age, email, password, role);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void accessingSchemaInDatabase() {
        System.out.println("\nAccessing Schema in Database:");
        System.out.println("Name (public): " + name);
        System.out.println("Age (default): " + age);
        System.out.println("Email (protected): " + email);
        System.out.println("Password (protected): " + password);    
        System.out.println("Role (protected): " + role);
        System.out.println("PrivateData (private): Cannot access\n\n");
    }
}

