package org.zeusagents.agents.input.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.input.InputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

@Builder
public class SenderCore {

    private InputAgent myInputAgent;

    public void sendMessageCyclicBehaviour(Behaviour behaviour){ this.sendCyclicMessage(behaviour);}
    public int sendMessageSimpleBehaviour(Behaviour behaviour, int receivedCount){ return sendSimpleMessage(behaviour, receivedCount);}
    public void sendMessageTickBehaviour(Behaviour behaviour){this.sendTickMessage(behaviour);}

    private void sendCyclicMessage(Behaviour behaviour){
        System.out.println("[Input Agent] Cyclic Behavior executing. ");
        while (!myInputAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage inputMsg = myInputAgent.getMessageCacheQueue().poll();

            if (inputMsg != null ) {
                if(myInputAgent.getLoadBalance() == null ){
                    String receiverAgent = deserializeMessage(inputMsg);
                    if(receiverAgent != null){
                        redirectMessage(inputMsg, receiverAgent);
                    }
                } else {
                    redirectMessage(inputMsg, myInputAgent.getLoadBalance().getAgent());
                }

            } else {
                System.out.println("[Input Agent] No message received, blocking");
            }
        }
        myInputAgent.removeBehaviour(behaviour);
    }

    private int sendSimpleMessage(Behaviour behaviour, int receivedCount){
        System.out.println("[Input Agent] Simple Behavior executing. ");
        if (!myInputAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage inputMsg = myInputAgent.getMessageCacheQueue().poll();

            if (inputMsg != null ) {
                if(myInputAgent.getLoadBalance() == null ){
                    String receiverAgent = deserializeMessage(inputMsg);
                    if(receiverAgent != null){
                        redirectMessage(inputMsg, receiverAgent);
                    }
                } else {
                    redirectMessage(inputMsg, myInputAgent.getLoadBalance().getAgent());
                }
                receivedCount++;
            } else {
                System.out.println("[Input Agent] No message received, blocking");
            }

        } else {
            behaviour.block();
        }
        return receivedCount;
    }

    private void sendTickMessage(Behaviour behaviour){
        //System.out.println("[ Input Agent] Tick Behavior executing. ");
        if (!myInputAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage inputMsg = myInputAgent.getMessageCacheQueue().poll();

            if (inputMsg != null ) {
                if(myInputAgent.getLoadBalance() == null ){
                    String receiverAgent = deserializeMessage(inputMsg);
                    if(receiverAgent != null){
                        redirectMessage(inputMsg, receiverAgent);
                    }
                } else {
                    redirectMessage(inputMsg, myInputAgent.getLoadBalance().getAgent());
                }

            } else {
                System.out.println("[Input Agent] No message received, blocking");
            }

        } else {
            behaviour.block();
        }
    }

    private String deserializeMessage(ACLMessage inputMsg){
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(inputMsg.getByteSequenceContent()))) {
            BasicMessageInputAgent data = (BasicMessageInputAgent) ois.readObject();
            System.out.println("[Input Agent] Received: " + data.getMiddleAgentReceiver() +
                    " Content: " + data.getContent());
            return data.getMiddleAgentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void redirectMessage(ACLMessage inputMsg, String receiverAgent) {
        System.out.println("[Input Agent] Rework message: " + inputMsg.getOntology() + ", To: " + receiverAgent);
        inputMsg.setSender(this.myInputAgent.getAID());
        inputMsg.clearAllReceiver();
        inputMsg.addReceiver(new AID(receiverAgent, AID.ISLOCALNAME));
        this.myInputAgent.send(inputMsg);
        System.out.println("[Input Agent] Send message: " + inputMsg.getOntology() + ", To: " + receiverAgent);
    }
}
