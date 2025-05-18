package org.zeusagents.agents.input.behaviours.tick;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.input.data.BasicMessageInputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class TickSenderInputBehaviourOpenAI extends TickerBehaviour {
    private String middelAgentName;

    @Builder
    public TickSenderInputBehaviourOpenAI(Agent inputAgent, String middelAgentName) {
        super(inputAgent,1000);
        this.middelAgentName = middelAgentName;
    }

    @Override
    protected void onTick() {
        InputOpenAIAgent myInputAgent = (InputOpenAIAgent) myAgent;

        if (!myInputAgent.getCustomMessagecache().get(this.middelAgentName).isEmpty()) {
            System.out.println("[Input OpenAPI Agent] Queued message: ");
            myInputAgent.getCustomMessagecache().get(this.middelAgentName).forEach(m -> System.out.println(m.getOntology()));
            ACLMessage inputMsg = myInputAgent.getCustomMessagecache().get(this.middelAgentName).poll();

            if(inputMsg != null && shouldSendMsg(inputMsg)){
                System.out.println("[Input OpenAPI Agent] Rework message: " + inputMsg.getOntology() + ", To: " + this.middelAgentName);
                inputMsg.setSender(this.myAgent.getAID());
                inputMsg.clearAllReceiver();
                inputMsg.addReceiver(new AID(this.middelAgentName, AID.ISLOCALNAME));
                this.myAgent.send(inputMsg);
                System.out.println("[Input OpenAPI Agent] Send message: " + inputMsg.getOntology() + ", To: " + this.middelAgentName);
            }
        }
    }

    private boolean shouldSendMsg(ACLMessage inputMsg){
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(inputMsg.getByteSequenceContent()))) {
            BasicMessageInputAgent data = (BasicMessageInputAgent) ois.readObject();
            System.out.println("[Input OpenAPI Agent] Received: " + data.getMiddleAgentReceiver() +
                    ", Content: " + data.getContent() +", Ontology: "+ inputMsg.getOntology());
            return this.middelAgentName.equals(data.getMiddleAgentReceiver());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
