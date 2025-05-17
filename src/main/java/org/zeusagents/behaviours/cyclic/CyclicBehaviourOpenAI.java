package org.zeusagents.behaviours.cyclic;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.zeusagents.openai.OpenAIClient;

@Builder
@Slf4j
public class CyclicBehaviourOpenAI extends CyclicBehaviour {

    OpenAIClient openAIClient;

    Agent agent;

    @Override
    public void action() {
        System.out.println("[Agent] Behavior executing");

        // Use MatchAll to see any incoming message
        ACLMessage msg = (ACLMessage) agent.getO2AObject();

        if (msg != null) {
            System.out.println("=== Received Message ===");
            System.out.println("Ontology: " + msg.getOntology());
            System.out.println("Content: " + msg.getContent());
            System.out.println("Performative: " + ACLMessage.getPerformative(msg.getPerformative()));
            System.out.println("=======================");
        } else {
            System.out.println("[Agent] No message received, blocking");
            block();
        }
    }
}
