package org.zeusagents.agents.monitor;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import lombok.Getter;
import org.zeusagents.agents.monitor.behaviours.monitor.AgentsMonitoringBehaviour;
import org.zeusagents.agents.monitor.config.MonitorConfig;

@Getter
public class MonitorAgent extends Agent {

    private MonitorConfig monitorConfig;

    protected void setup() {
        System.out.println("[Manager Agent] ManagerAgent " + getAID().getName() + " is ready");
        Object[] args = getArguments();
        this.monitorConfig = (MonitorConfig) args[0];


        for (AgentController inputAgent : this.monitorConfig.getInputAgentList()) {
            try {
                String[] inputLocalName = inputAgent.getName().split("@");
                System.out.println("[Manager Agent] " + getLocalName() + ": Subscribing to publisher, " + inputAgent.getName());
                ACLMessage subscribe = new ACLMessage(ACLMessage.SUBSCRIBE);
                subscribe.addReceiver(new AID(inputLocalName[0], AID.ISLOCALNAME));
                subscribe.setContent("SUBSCRIBE");
                addBehaviour(AgentsMonitoringBehaviour.builder().a(this).msg(subscribe).build());
            } catch (StaleProxyException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
