package org.zeusagents.agents.middle.behaviours.main;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.agents.middle.MiddleAgent;


public class CyclicMiddleMainBehaviour extends CyclicBehaviour {

    @Builder
    public CyclicMiddleMainBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        System.out.println("[Middle OpenAPI Agent " + myAgent.getName() + "] Behavior executing. Message QueueSize: " + myAgent.getCurQueueSize());

        ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        if (msg != null) {
            MiddleAgent midAgent = (MiddleAgent) this.myAgent;
            midAgent.getMessageCacheQueue().add(msg);
        } else {
            System.out.println("[Middle OpenAPI Agent] No message received, blocking");
            block();
        }
    }
}
