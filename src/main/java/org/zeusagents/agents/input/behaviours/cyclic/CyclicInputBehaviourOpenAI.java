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


@Slf4j
public class CyclicInputBehaviourOpenAI extends CyclicBehaviour {

    private String middelAgentName;

    @Builder
    public CyclicInputBehaviourOpenAI(Agent inputAgent, String middelAgentName) {
        super(inputAgent);
        this.middelAgentName = middelAgentName;
    }

    @Override
    public void action() {
        System.out.println("[Input OpenAPI Agent] Behavior executing. Message QueueSize: " + myAgent.getCurQueueSize());

        ACLMessage inputMsg = (ACLMessage) myAgent.getO2AObject();

        if (inputMsg != null && shouldSendMsg(inputMsg)) {
            inputMsg.setSender(this.myAgent.getAID());
            inputMsg.clearAllReceiver();
            inputMsg.addReceiver(new AID(this.middelAgentName, AID.ISLOCALNAME));
            this.myAgent.send(inputMsg);
        } else {
            System.out.println("[Input OpenAPI Agent] No message received, blocking");
        }
        block();
    }

    private boolean shouldSendMsg(ACLMessage inputMsg){
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(inputMsg.getByteSequenceContent()))) {
            BasicMessageInputAgent data = (BasicMessageInputAgent) ois.readObject();
            System.out.println("[Input OpenAPI Agent] Received: " + data.getMiddleAgentReceiver() +
                    " Content: " + data.getContent());
            return this.middelAgentName.equals(data.getMiddleAgentReceiver());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
