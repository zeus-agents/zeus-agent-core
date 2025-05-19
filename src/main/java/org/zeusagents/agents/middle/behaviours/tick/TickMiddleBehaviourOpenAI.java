package org.zeusagents.agents.middle.behaviours.tick;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.data.BasicMessageInputAgent;
import org.zeusagents.openai.OpenAIClient;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class TickMiddleBehaviourOpenAI extends TickerBehaviour {

    private OpenAIClient openAIClient;

    @Builder
    public TickMiddleBehaviourOpenAI(Agent agent, OpenAIClient openAIClient, long period) {
        super(agent, period);
        this.openAIClient = openAIClient;
    }

    @Override
    protected void onTick() {

        ACLMessage msg = myAgent.blockingReceive();
        //ACLMessage msg = myAgent.blockingReceive(1000); // Waits up to 1 second for messages
        BasicMessageInputAgent data = null;

        if (msg != null) {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new ByteArrayInputStream(msg.getByteSequenceContent()))) {
                data = (BasicMessageInputAgent) ois.readObject();
                System.out.println("[Middle OpenAPI Agent "+ myAgent.getName()+"] Received: " + data.getMiddleAgentReceiver() +
                        " Content: " + data.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("=== Received Message ===");
            System.out.println("Sender: " + msg.getSender().getName());
            System.out.println("Ontology: " + msg.getOntology());
            System.out.println("Content: " + data.getContent());
            System.out.println("Performative: " + ACLMessage.getPerformative(msg.getPerformative()));
            System.out.println("=======================");
        }
    }
}
