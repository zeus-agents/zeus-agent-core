package org.zeusagents.agents.middle;

import jade.core.Agent;
import org.zeusagents.agents.middle.behaviours.cyclic.CyclicMiddleBehaviourOpenAI;
import org.zeusagents.openai.OpenAITextGeneratorClient;

public class MiddleOpenAIAgent extends Agent {

    protected void setup() {
        System.out.println("ReceiverAgent " + getAID().getName() + " is ready");

        addBehaviour(CyclicMiddleBehaviourOpenAI.builder().agent(this).openAIClient(new OpenAITextGeneratorClient()).build());
    }
}
