package org.zeusagents.agents.input.behaviours.cyclic;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;


@Builder
@Slf4j
public class CyclicInputBehaviourOpenAI extends CyclicBehaviour {

    Agent inputAgent;

    String middelAgentName;

    @Override
    public void action() {
        System.out.println("[Input OpenAPI Agent] Behavior executing");

        ACLMessage inputMsg = (ACLMessage) inputAgent.getO2AObject();

        if (inputMsg != null) {
            ACLMessage middleMsg = new ACLMessage(ACLMessage.REQUEST);
            middleMsg.setSender(this.inputAgent.getAID());
            middleMsg.addReceiver(new AID(this.middelAgentName, AID.ISLOCALNAME));
            middleMsg.setContent(inputMsg.getContent());
            System.out.println("[Input OpenAPI Agent] Send message to: " + middelAgentName);
            this.inputAgent.send(middleMsg);

        } else {
            System.out.println("[Input OpenAPI Agent] No message received, blocking");
            block();
        }
    }
}
