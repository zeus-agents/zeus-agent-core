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

    @Builder
    public TickSenderInputBehaviourOpenAI(Agent inputAgent) {
        super(inputAgent,1000);
    }

    @Override
    protected void onTick() {
        InputOpenAIAgent myInputAgent = (InputOpenAIAgent) myAgent;

        if (!myInputAgent.getMessageCacheQueue().isEmpty()) {
            System.out.println("[Input OpenAPI Agent] Queued message: ");
            myInputAgent.getMessageCacheQueue().forEach(m -> System.out.println(m.getOntology()));

            ACLMessage inputMsg = myInputAgent.getMessageCacheQueue().poll();

            String receiverAgent = getReceiverAgent(inputMsg);

            if (inputMsg != null && receiverAgent != null) {
                System.out.println("[Input OpenAPI Agent] Rework message: " + inputMsg.getOntology() + ", To: " + receiverAgent);
                inputMsg.setSender(this.myAgent.getAID());
                inputMsg.clearAllReceiver();
                inputMsg.addReceiver(new AID(receiverAgent, AID.ISLOCALNAME));
                this.myAgent.send(inputMsg);
                System.out.println("[Input OpenAPI Agent] Send message: " + inputMsg.getOntology() + ", To: " + receiverAgent);
            }
        }
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
