package lib.gintec_rdl.momo.model;

public final class MpambaCashInTransaction extends Transaction {

    private MobileMoneyAgent agent;
    private double amount = 0D, fee = 0D, balance = 0D;

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
