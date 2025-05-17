package org.zeusagents.agents.middle.behaviours.simple;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.openai.OpenAIClient;

public class SimpleMiddleBehaviourOpenAI extends SimpleBehaviour {

    private int receivedCount = 0;
    private OpenAIClient openAIClient;

    @Builder
    public SimpleMiddleBehaviourOpenAI(Agent agent, OpenAIClient openAIClient) {
        super(agent);
        this.openAIClient = openAIClient;
    }

    @Override
    public void action() {
        System.out.println("[Middle OpenAPI Agent] Behavior executing");
        // Use MatchAll to see any incoming message
        ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        //ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        if (msg != null) {
            System.out.println("=== Received Message ===");
            System.out.println("Sender: " + msg.getSender().getName());
            System.out.println("Ontology: " + msg.getOntology());
            System.out.println("Content: " + msg.getContent());
            System.out.println("Performative: " + ACLMessage.getPerformative(msg.getPerformative()));
            System.out.println("=======================");
            receivedCount++;
        } else {
            System.out.println("[Middle OpenAPI Agent] No message received, blocking");
            block();
        }
    }

    @Override
    public boolean done() {
        if (receivedCount >= 1) {
            System.out.println(myAgent.getLocalName() + " finished processing");
            return true;
        }
        return false;
    }
}
