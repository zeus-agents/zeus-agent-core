package org.zeusagents.agents.input.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.input.InputOpenAIAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

@Builder
public class SenderCore {

    private InputOpenAIAgent myInputAgent;

    public void sendMessageCyclicBehaviour(Behaviour behaviour){ this.sendCyclicMessage(behaviour);}
    public int sendMessageSimpleBehaviour(Behaviour behaviour, int receivedCount){ return sendSimpleMessage(behaviour, receivedCount);}
    public void sendMessageTickBehaviour(Behaviour behaviour){this.sendTickMessage(behaviour);}

    private void sendCyclicMessage(Behaviour behaviour){
        System.out.println("[Input OpenAPI Agent] Behavior executing. ");

        while (!myInputAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage inputMsg = myInputAgent.getMessageCacheQueue().poll();
            String receiverAgent = deserializeMessage(inputMsg);

            if (inputMsg != null && receiverAgent != null) {
                redirectMessage(inputMsg, receiverAgent);
            } else {
                System.out.println("[Input OpenAPI Agent] No message received, blocking");
            }
        }
        myInputAgent.removeBehaviour(behaviour);
    }

    private int sendSimpleMessage(Behaviour behaviour, int receivedCount){
        if (!myInputAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage inputMsg = myInputAgent.getMessageCacheQueue().poll();
            String receiverAgent = deserializeMessage(inputMsg);

            if (inputMsg != null && receiverAgent != null) {
                redirectMessage(inputMsg, receiverAgent);
                receivedCount++;
            }
        } else {
            behaviour.block();
        }
        return receivedCount;
    }

    private void sendTickMessage(Behaviour behaviour){

        if (!myInputAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage inputMsg = myInputAgent.getMessageCacheQueue().poll();
            String receiverAgent = deserializeMessage(inputMsg);

            if (inputMsg != null && receiverAgent != null) {
                redirectMessage(inputMsg, receiverAgent);
            }
        } else {
            behaviour.block();
        }
    }

    private String deserializeMessage(ACLMessage inputMsg){
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

    private void redirectMessage(ACLMessage inputMsg, String receiverAgent) {
        System.out.println("[Input OpenAPI Agent] Rework message: " + inputMsg.getOntology() + ", To: " + receiverAgent);
        inputMsg.setSender(this.myInputAgent.getAID());
        inputMsg.clearAllReceiver();
        inputMsg.addReceiver(new AID(receiverAgent, AID.ISLOCALNAME));
        this.myInputAgent.send(inputMsg);
        System.out.println("[Input OpenAPI Agent] Send message: " + inputMsg.getOntology() + ", To: " + receiverAgent);
    }
}
