package org.zeusagents.agents.middle.behaviours.main;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.agents.middle.MiddleAgent;

public class SimpleMiddleMainBehaviour extends SimpleBehaviour {

    private int receivedCount = 0;
    private final int maxReceived;

    @Builder
    public SimpleMiddleMainBehaviour(Agent agent, int maxReceived) {
        super(agent);
        this.maxReceived=maxReceived;
    }

    @Override
    public void action() {
        System.out.println("[Middle  Agent] Behavior executing");

        // Use MatchAll to see any incoming message
        ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        if (msg != null) {
            MiddleAgent midAgent = (MiddleAgent) this.myAgent;
            midAgent.getMessageCacheQueue().add(msg);
            this.receivedCount++;
        } else {
            System.out.println("[Middle  Agent] No message received, blocking");
            block();
        }
    }

    @Override
    public boolean done() {
        if (this.receivedCount >= this.maxReceived) {
            System.out.println(myAgent.getLocalName() + " finished processing");
            return true;
        }
        return false;
    }
}
