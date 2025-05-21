package org.zeusagents.agents.middle.behaviours.main;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.agents.middle.MiddleOpenAIAgent;
import org.zeusagents.agents.middle.behaviours.schema.MiddleFSMBehaviour;


public class CyclicMiddleMainBehaviour extends CyclicBehaviour {

    @Builder
    public CyclicMiddleMainBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        System.out.println("[Middle OpenAPI Agent " + myAgent.getName() + "] Behavior executing. Message QueueSize: " + myAgent.getCurQueueSize());

        ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        if (msg != null) {
            MiddleOpenAIAgent midAgent = (MiddleOpenAIAgent) this.myAgent;
            midAgent.getMessageCacheQueue().add(msg);
            midAgent.addBehaviour(MiddleFSMBehaviour.builder().midAgent(this.myAgent).build());
        } else {
            System.out.println("[Middle OpenAPI Agent] No message received, blocking");
        }
        block();
    }
}
