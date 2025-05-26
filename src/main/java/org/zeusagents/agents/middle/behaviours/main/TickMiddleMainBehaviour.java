package org.zeusagents.agents.middle.behaviours.main;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.agents.middle.MiddleAgent;

public class TickMiddleMainBehaviour extends TickerBehaviour {


    @Builder
    public TickMiddleMainBehaviour(Agent agent, long period) {
        super(agent, period);
    }

    @Override
    protected void onTick() {

        ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        //ACLMessage msg = myAgent.blockingReceive(1000); // Waits up to 1 second for messages

        if (msg != null) {
            MiddleAgent midAgent = (MiddleAgent) this.myAgent;
            midAgent.getMessageCacheQueue().add(msg);
        } else {
            System.out.println("[Middle OpenAPI Agent] No message received, blocking");
            block();
        }
    }
}
