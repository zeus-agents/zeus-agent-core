package org.zeusagents.agents.middle.behaviours.cyclic;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.zeusagents.openai.OpenAIClient;

@Builder
@Slf4j
public class CyclicMiddleBehaviourOpenAI extends CyclicBehaviour {

    private OpenAIClient openAIClient;

    private Agent agent;

    @Override
    public void action() {
        System.out.println("[Middle OpenAPI Agent] Behavior executing");

        // Use MatchAll to see any incoming message
        ACLMessage msg = agent.receive();

        if (msg != null) {
            System.out.println("=== Received Message ===");
            System.out.println("Ontology: " + msg.getSender().getName());
            System.out.println("Ontology: " + msg.getOntology());
            System.out.println("Content: " + msg.getContent());
            System.out.println("Performative: " + ACLMessage.getPerformative(msg.getPerformative()));
            System.out.println("=======================");
        } else {
            System.out.println("[Middle OpenAPI Agent] No message received, blocking");
            block();
        }
    }
}
