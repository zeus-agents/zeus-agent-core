package org.zeusagents.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.zeusagents.behaviours.cyclic.CyclicBehaviourOpenAI;
import org.zeusagents.openai.OpenAITextGeneratorClient;

@Slf4j
public class InputOpenAIAgent extends Agent {

    protected void setup() {
        System.out.println("ReceiverAgent " + getAID().getName() + " is ready");

        //This accept data external from the JDE system
        setEnabledO2ACommunication(true, 3);

        // Add behavior to handle different message types
        /*addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                System.out.println("[Agent] Behavior executing");

                // Use MatchAll to see any incoming message
                ACLMessage msg = (ACLMessage) getO2AObject();

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
        });*/

        addBehaviour(CyclicBehaviourOpenAI.builder().agent(this).openAIClient(new OpenAITextGeneratorClient()).build());
        System.out.println("[Agent] SETUP COMPLETE");
    }
}
