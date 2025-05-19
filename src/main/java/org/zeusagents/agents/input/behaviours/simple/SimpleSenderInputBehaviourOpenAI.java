package org.zeusagents.agents.input.behaviours.simple;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.data.BasicMessageInputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class SimpleSenderInputBehaviourOpenAI extends SimpleBehaviour {

    private int receivedCount = 0;
    private int maxReceived;

    @Builder
    public SimpleSenderInputBehaviourOpenAI(Agent inputAgent, int maxReceived) {
        super(inputAgent);
        this.maxReceived= maxReceived;
    }

    @Override
    public void action() {
        InputOpenAIAgent myInputAgent = (InputOpenAIAgent) myAgent;

        if (!myInputAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage inputMsg = myInputAgent.getMessageCacheQueue().poll();

            String receiverAgent = getReceiverAgent(inputMsg);

            if (inputMsg != null && receiverAgent != null) {
                inputMsg.setSender(this.myAgent.getAID());
                inputMsg.clearAllReceiver();
                inputMsg.addReceiver(new AID(receiverAgent, AID.ISLOCALNAME));
                this.myAgent.send(inputMsg);
                System.out.println("[Input OpenAPI Agent] Send message: " + inputMsg.getOntology() + ", To: " + receiverAgent);
                receivedCount++;
            } else {
                System.out.println("[Input OpenAPI Agent] No message received, blocking");
                block();
            }
        }
    }

    @Override
    public boolean done() {
        if (receivedCount >= maxReceived) {
            System.out.println(myAgent.getLocalName() + " finished processing");
            return true;
        }
        return false;
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
