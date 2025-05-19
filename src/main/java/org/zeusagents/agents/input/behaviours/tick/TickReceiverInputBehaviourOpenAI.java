package org.zeusagents.agents.input.behaviours.tick;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.data.BasicMessageInputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class TickReceiverInputBehaviourOpenAI extends TickerBehaviour {


    @Builder
    public TickReceiverInputBehaviourOpenAI(Agent inputAgent, long period) {
        super(inputAgent,period);
    }

    @Override
    protected void onTick() {
        ACLMessage inputMsg = (ACLMessage) myAgent.getO2AObject();
        if(inputMsg != null){

            try (ObjectInputStream ois =
                         new ObjectInputStream(new ByteArrayInputStream(inputMsg.getByteSequenceContent()))) {

                BasicMessageInputAgent data = (BasicMessageInputAgent) ois.readObject();

                InputOpenAIAgent myInputAgent = (InputOpenAIAgent) myAgent;
                myInputAgent.getMessageCacheQueue().add(inputMsg);

                System.out.println("[Input OpenAPI Agent] Save Ontology: "+inputMsg.getOntology()+", Agent: "+data.getMiddleAgentReceiver());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
