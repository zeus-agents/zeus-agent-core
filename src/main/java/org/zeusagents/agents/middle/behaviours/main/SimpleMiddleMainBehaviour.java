package org.zeusagents.agents.middle.behaviours.main;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.AIClient.AIClient;
import org.zeusagents.agents.middle.MiddleOpenAIAgent;
import org.zeusagents.agents.middle.behaviours.schema.MiddleFSMBehaviour;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

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
        System.out.println("[Middle OpenAPI Agent] Behavior executing");

        // Use MatchAll to see any incoming message
        ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        if (msg != null) {
            MiddleOpenAIAgent midAgent = (MiddleOpenAIAgent) this.myAgent;
            midAgent.getMessageCacheQueue().add(msg);
            midAgent.addBehaviour(MiddleFSMBehaviour.builder().midAgent(this.myAgent).period(midAgent.getMiddleMainConfig().getFsmPeriod()).build());
            this.receivedCount++;
        } else {
            System.out.println("[Middle OpenAPI Agent] No message received, blocking");
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
