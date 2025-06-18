package org.zeusagents.agents.manager;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import lombok.Getter;
import org.zeusagents.agents.input.InputAgent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AgentManager extends Agent {

    private List<InputAgent> inputAgentList;

    protected void setup() {
        System.out.println("[Manager Agent] ManagerAgent " + getAID().getName() + " is ready");
        inputAgentList = new ArrayList<>();

        System.out.println(getLocalName() + ": Subscribing to publisher");

        ACLMessage subscribe = new ACLMessage(ACLMessage.SUBSCRIBE);
        subscribe.addReceiver(new AID("PublisherAgent", AID.ISLOCALNAME));
        subscribe.setContent("Please notify me");

        addBehaviour(new SubscriptionInitiator(this, subscribe) {
            @Override
            protected void handleInform(ACLMessage inform) {
                System.out.println(getLocalName() + ": Received update: " + inform.getContent());
            }
        });
    }
}
