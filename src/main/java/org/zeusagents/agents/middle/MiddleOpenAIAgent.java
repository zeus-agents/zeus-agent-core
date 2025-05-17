package org.zeusagents.agents.middle;

import jade.core.Agent;
import org.zeusagents.agents.input.behaviours.simple.SimpleInputBehaviourOpenAI;
import org.zeusagents.agents.middle.behaviours.cyclic.CyclicMiddleBehaviourOpenAI;
import org.zeusagents.agents.middle.behaviours.simple.SimpleMiddleBehaviourOpenAI;
import org.zeusagents.agents.middle.config.MiddleBehaviourType;
import org.zeusagents.agents.middle.config.MiddleOpenAIConfig;
import org.zeusagents.openai.OpenAITextGeneratorClient;

public class MiddleOpenAIAgent extends Agent {

    private MiddleOpenAIConfig middleOpenAIConfig;

    protected void setup() {
        System.out.println("[Middle OpenAPI Agent] ReceiverAgent " + getAID().getName() + " is ready");
        final Object[] args = getArguments();

        if(null != args){
            middleOpenAIConfig = (MiddleOpenAIConfig) args[0];
            activateBehaviours();
            System.out.println("[Middle OpenAPI Agent] SETUP COMPLETE");
        } else {
            System.out.println("[Middle OpenAPI Agent] SETUP ERROR!");
        }
    }

    private void activateBehaviours(){
        if(middleOpenAIConfig.getMiddleBehaviourType().equals(MiddleBehaviourType.CYCLIC_MIDDLE_BEHAVIOUR_OPENAI)){
            addBehaviour(CyclicMiddleBehaviourOpenAI.builder().agent(this).openAIClient(middleOpenAIConfig.getOpenAIClient()).build());
        }

        if(middleOpenAIConfig.getMiddleBehaviourType().equals(MiddleBehaviourType.SIMPLE_MIDDLE_BEHAVIOUR_OPENAI)){
            addBehaviour(SimpleMiddleBehaviourOpenAI.builder().agent(this).openAIClient(middleOpenAIConfig.getOpenAIClient()).build());
        }
    }
}
