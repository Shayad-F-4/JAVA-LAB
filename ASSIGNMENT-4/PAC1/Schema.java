package PAC1;

import java.util.*;

public class Schema {
    public String name;
    int age;
    protected String email;
    protected String password;
    protected String role;
    private String privateData; // Can only be accessed within this class

    public Schema(String name, int age, String email, String password, String role) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}