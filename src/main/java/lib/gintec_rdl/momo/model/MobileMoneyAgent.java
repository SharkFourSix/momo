package lib.gintec_rdl.momo.model;

public final class MobileMoneyAgent {

    private String agentCode;
    private String agentName;

    public MobileMoneyAgent(String agentCode, String agentName) {
        this.agentCode = agentCode;
        this.agentName = agentName;
    }

    public MobileMoneyAgent() {
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public String toString() {
        return agentCode + " - " + agentName;
    }
}
