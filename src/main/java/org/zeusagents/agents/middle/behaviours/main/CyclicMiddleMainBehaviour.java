package org.zeusagents.agents.middle.behaviours.main;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.agents.middle.MiddleOpenAIAgent;
import org.zeusagents.openai.AIClient;


public class CyclicMiddleMainBehaviour extends CyclicBehaviour {

    private AIClient AIClient;

    @Builder
    public CyclicMiddleMainBehaviour(Agent agent, AIClient AIClient) {
        super(agent);
        this.AIClient = AIClient;
    }

    @Override
    public void action() {
        System.out.println("[Middle OpenAPI Agent " + myAgent.getName() + "] Behavior executing. Message QueueSize: " + myAgent.getCurQueueSize());

        ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        MiddleOpenAIAgent midAgent = (MiddleOpenAIAgent) this.myAgent;
        midAgent.getMessageCacheQueue().add(msg);
        midAgent.addBehaviour(CyclicFSMiddleMainBehaviour.builder().midAgent(this.myAgent).build());

        block();
    }
}
