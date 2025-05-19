package org.zeusagents.agents.input.behaviours.cyclic;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.data.BasicMessageInputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;


public class CyclicSenderInputBehaviourOpenAI extends CyclicBehaviour {

    @Builder
    public CyclicSenderInputBehaviourOpenAI(Agent inputAgent) {
        super(inputAgent);
    }

    @Override
    public void action() {
        System.out.println("[Input OpenAPI Agent] Behavior executing. ");

        InputOpenAIAgent myInputAgent = (InputOpenAIAgent) myAgent;

        while (!myInputAgent.getMessageCacheQueue().isEmpty()) {
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
            } else {
                System.out.println("[Input OpenAPI Agent] No message received, blocking");
            }
        }
        myAgent.removeBehaviour(this);

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
