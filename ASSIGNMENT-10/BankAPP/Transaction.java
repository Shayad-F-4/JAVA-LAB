import java.io.Serializable;
import java.time.LocalDateTime;

class Transaction implements Serializable {

    String type;
    double amount;
    LocalDateTime time;

    Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.time = LocalDateTime.now();
    }

    public String toString() {
        return time + " | " + type + " | ₹" + amount;
    }
}