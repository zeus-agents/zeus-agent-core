package org.zeusagents.agents.input.behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.input.InputAgent;
import org.zeusagents.agents.input.behaviours.cyclic.CyclicSenderInputBehaviour;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

@Builder
public class ReceiverCore {

    private Agent myAgent;

    public void receiveMessageCyclicBehaviour(){
        this.receiveCyclicMessage();
    }

    public int receiveMessageSimpleBehaviour(int receivedCount){
        return this.receiveSimpleMessage(receivedCount);
    }

    public void receiveMessageTickBehaviour(){
        this.receiveTickMessage();
    }


    private void receiveCyclicMessage(){
        ACLMessage inputMsg = (ACLMessage) myAgent.getO2AObject();

        if(inputMsg != null){
            this.deserializeMessage(inputMsg);
            myAgent.addBehaviour(CyclicSenderInputBehaviour.builder().inputAgent(myAgent).build());
        }
    }

    private int receiveSimpleMessage(int receivedCount){
        ACLMessage inputMsg = (ACLMessage) myAgent.getO2AObject();

        if(inputMsg != null){
            this.deserializeMessage(inputMsg);
            receivedCount++;
        }
        return receivedCount;
    }

    private void receiveTickMessage(){
        ACLMessage inputMsg = (ACLMessage) myAgent.getO2AObject();

        if(inputMsg != null){
            this.deserializeMessage(inputMsg);
        }
    }

    private void deserializeMessage(ACLMessage inputMsg){
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(inputMsg.getByteSequenceContent()))) {

            BasicMessageInputAgent data = (BasicMessageInputAgent) ois.readObject();

            InputAgent myInputAgent = (InputAgent) myAgent;
            myInputAgent.getMessageCacheQueue().add(inputMsg);
            System.out.println("[Input OpenAPI Agent] Save Ontology: "+inputMsg.getOntology()+", Agent: "+data.getMiddleAgentReceiver());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
