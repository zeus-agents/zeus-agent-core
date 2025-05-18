package org.zeusagents.agents.middle.behaviours.cyclic;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.zeusagents.agents.input.data.BasicMessageInputAgent;
import org.zeusagents.openai.OpenAIClient;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;


public class CyclicMiddleBehaviourOpenAI extends CyclicBehaviour {

    private OpenAIClient openAIClient;

    @Builder
    public CyclicMiddleBehaviourOpenAI(Agent agent, OpenAIClient openAIClient) {
        super(agent);
        this.openAIClient = openAIClient;
    }


    @Override
    public void action() {
        System.out.println("[Middle OpenAPI Agent " + myAgent.getName()+ "] Behavior executing. Message QueueSize: " + myAgent.getCurQueueSize());
        BasicMessageInputAgent data = null;
        // Use MatchAll to see any incoming message

        //ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

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
        } else {
            System.out.println("[Middle OpenAPI Agent "+ myAgent.getName()+"] No message received, blocking");
        }
        block();
    }
}
