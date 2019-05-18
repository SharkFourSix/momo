package lib.gintec_rdl.momo.model;

public class MpambaCashOutTransaction extends Transaction {
    private MobileMoneyAgent agent;
    private double amount, fee, balance;

    public MobileMoneyAgent getAgent() {
        return agent;
    }

    public void setAgent(MobileMoneyAgent agent) {
        this.agent = agent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
