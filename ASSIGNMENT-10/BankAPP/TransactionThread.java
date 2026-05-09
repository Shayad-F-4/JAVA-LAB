class TransactionThread extends Thread {

    Account acc;
    double amt;
    boolean isDeposit;

    TransactionThread(Account acc, double amt, boolean isDeposit) {
        this.acc = acc;
        this.amt = amt;
        this.isDeposit = isDeposit;
    }

    public void run() {
        try {
            if (isDeposit)
                acc.deposit(amt);
            else
                acc.withdraw(amt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}