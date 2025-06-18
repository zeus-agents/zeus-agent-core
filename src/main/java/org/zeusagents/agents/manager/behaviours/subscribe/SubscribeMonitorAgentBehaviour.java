package org.zeusagents.agents.manager.behaviours.subscribe;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionInitiator;
import lombok.Builder;

public class SubscribeMonitorAgentBehaviour extends SubscriptionInitiator {

    @Builder
    public SubscribeMonitorAgentBehaviour(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    public void handleInform() {
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE));
    }

}
