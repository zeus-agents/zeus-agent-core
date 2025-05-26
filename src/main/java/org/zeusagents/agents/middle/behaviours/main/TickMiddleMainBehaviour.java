package org.zeusagents.agents.middle.behaviours.main;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.AIClient.AIClient;
import org.zeusagents.agents.middle.MiddleOpenAIAgent;
import org.zeusagents.agents.middle.behaviours.schema.MiddleFSMBehaviour;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class TickMiddleMainBehaviour extends TickerBehaviour {


    @Builder
    public TickMiddleMainBehaviour(Agent agent, long period) {
        super(agent, period);
    }

    @Override
    protected void onTick() {

        ACLMessage msg = myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        //ACLMessage msg = myAgent.blockingReceive(1000); // Waits up to 1 second for messages
        BasicMessageInputAgent data = null;

        if (msg != null) {
            MiddleOpenAIAgent midAgent = (MiddleOpenAIAgent) this.myAgent;
            midAgent.getMessageCacheQueue().add(msg);
            midAgent.addBehaviour(MiddleFSMBehaviour.builder().midAgent(this.myAgent).period(midAgent.getMiddleMainConfig().getFsmPeriod()).build());
        } else {
            System.out.println("[Middle OpenAPI Agent] No message received, blocking");
        }
    }
}
