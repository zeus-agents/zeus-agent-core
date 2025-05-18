package org.zeusagents.agents.input.behaviours.simple;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.input.data.BasicMessageInputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class SimpleReceiverInputBehaviourOpenAI  extends SimpleBehaviour {
    private int receivedCount = 0;

    @Builder
    public SimpleReceiverInputBehaviourOpenAI(Agent inputAgent) {
        super(inputAgent);
    }

    @Override
    public void action() {
        System.out.println("[Input OpenAPI Agent] Behavior executing");

        ACLMessage inputMsg = (ACLMessage) myAgent.getO2AObject();

        if(inputMsg != null){

            try (ObjectInputStream ois =
                         new ObjectInputStream(new ByteArrayInputStream(inputMsg.getByteSequenceContent()))) {

                BasicMessageInputAgent data = (BasicMessageInputAgent) ois.readObject();

                InputOpenAIAgent myInputAgent = (InputOpenAIAgent) myAgent;
                myInputAgent.getMessageCacheQueue().add(inputMsg);
                receivedCount++;

                System.out.println("[Input OpenAPI Agent] Save Ontology: "+inputMsg.getOntology()+", Agent: "+data.getMiddleAgentReceiver());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        block();
    }

    @Override
    public boolean done() {
        if (receivedCount >= 5) {
            System.out.println(myAgent.getLocalName() + " finished processing");
            return true;
        }
        return false;
    }
}
