package PAC2;

import PAC1.Schema;
import java.util.Scanner;

public class SupperBase extends Schema {
    
    public SupperBase() {
        super("Test", 20, "test@gmail.com", "1234", "User");
    }

    public void accessingSchemaInSupperBase() {
        System.out.println("\nAccessing schema in SupperBase...");
        
        System.out.println("Name (public): " + name);
        System.out.println("Age (default): Cannot access");
        System.out.println("Email (protected): " + email);
        System.out.println("Password (protected): " + password);    
        System.out.println("Role (protected): " + role);
        System.out.println("PrivateData (private): Cannot access(so no any input also from useer for testing perpose )\n\n");
    }
}