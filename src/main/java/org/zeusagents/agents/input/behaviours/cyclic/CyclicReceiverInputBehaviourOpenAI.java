package org.zeusagents.agents.input.behaviours.cyclic;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.input.data.BasicMessageInputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class CyclicReceiverInputBehaviourOpenAI extends CyclicBehaviour {

    @Builder
    public CyclicReceiverInputBehaviourOpenAI(Agent inputAgent) {
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

                myInputAgent.addBehaviour(CyclicSenderInputBehaviourOpenAI.builder().inputAgent(myInputAgent).build());
                System.out.println("[Input OpenAPI Agent] Save Ontology: "+inputMsg.getOntology()+", Agent: "+data.getMiddleAgentReceiver());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        block();
    }
}
