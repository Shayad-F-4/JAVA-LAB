import java.util.*;

class BankService {

    HashMap<Integer, Account> accounts = FileUtil.load();

    void create(int acc, String name, String pass, double bal) {
        accounts.put(acc, new Account(acc, name, pass, bal));
        FileUtil.save(accounts);
    }

    Account login(int acc, String pass) {
        Account a = accounts.get(acc);
        if (a != null && a.password.equals(pass))
            return a;
        return null;
    }
}