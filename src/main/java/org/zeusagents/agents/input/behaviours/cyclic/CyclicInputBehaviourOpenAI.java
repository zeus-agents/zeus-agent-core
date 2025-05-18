package org.zeusagents.agents.input.behaviours.cyclic;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.zeusagents.agents.input.data.BasicMessageInputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;


public class CyclicInputBehaviourOpenAI extends CyclicBehaviour {

    @Builder
    public CyclicInputBehaviourOpenAI(Agent inputAgent) {
        super(inputAgent);
    }

    @Override
    public void action() {
        System.out.println("[Input OpenAPI Agent] Behavior executing. Message QueueSize: " + myAgent.getCurQueueSize());

        ACLMessage inputMsg = (ACLMessage) myAgent.getO2AObject();

        String receiverAgent = getReceiverAgent(inputMsg);

        if (inputMsg != null && receiverAgent != null) {
            inputMsg.setSender(this.myAgent.getAID());
            inputMsg.clearAllReceiver();
            inputMsg.addReceiver(new AID(receiverAgent, AID.ISLOCALNAME));
            this.myAgent.send(inputMsg);
        } else {
            System.out.println("[Input OpenAPI Agent] No message received, blocking");
        }
        block();
    }

    private String getReceiverAgent(ACLMessage inputMsg){
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(inputMsg.getByteSequenceContent()))) {
            BasicMessageInputAgent data = (BasicMessageInputAgent) ois.readObject();
            System.out.println("[Input OpenAPI Agent] Received: " + data.getMiddleAgentReceiver() +
                    " Content: " + data.getContent());
            return data.getMiddleAgentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
