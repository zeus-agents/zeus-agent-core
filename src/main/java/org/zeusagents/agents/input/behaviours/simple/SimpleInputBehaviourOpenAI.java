package org.zeusagents.agents.input.behaviours.simple;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;

public class SimpleInputBehaviourOpenAI extends SimpleBehaviour {

    private String middelAgentName;
    private int receivedCount = 0;

    @Builder
    public SimpleInputBehaviourOpenAI(Agent inputAgent, String middelAgentName) {
        super(inputAgent);
        this.middelAgentName = middelAgentName;
    }

    @Override
    public void action() {
        System.out.println("[Input OpenAPI Agent] Behavior executing");

        ACLMessage inputMsg = (ACLMessage) myAgent.getO2AObject();

        if (inputMsg != null) {
            inputMsg.setSender(this.myAgent.getAID());
            inputMsg.clearAllReceiver();
            inputMsg.addReceiver(new AID(this.middelAgentName, AID.ISLOCALNAME));
            inputMsg.setContent(inputMsg.getContent());
            this.myAgent.send(inputMsg);
            receivedCount++;
        } else {
            System.out.println("[Input OpenAPI Agent] No message received, blocking");
            block();
        }

    }

    @Override
    public boolean done() {
        if (receivedCount >= 1) {
            System.out.println(myAgent.getLocalName() + " finished processing");
            return true;
        }
        return false;
    }
}
