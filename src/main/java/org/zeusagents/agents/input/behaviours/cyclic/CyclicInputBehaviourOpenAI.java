package org.zeusagents.agents.input.behaviours.cyclic;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CyclicInputBehaviourOpenAI extends CyclicBehaviour {

    String middelAgentName;

    @Builder
    public CyclicInputBehaviourOpenAI(Agent inputAgent, String middelAgentName) {
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

        } else {
            System.out.println("[Input OpenAPI Agent] No message received, blocking");
        }
        block();
    }
}
