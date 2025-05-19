package org.zeusagents.agents.middle;

import jade.core.Agent;
import org.zeusagents.agents.middle.behaviours.cyclic.CyclicMiddleBehaviourOpenAI;
import org.zeusagents.agents.middle.behaviours.simple.SimpleMiddleBehaviourOpenAI;
import org.zeusagents.agents.middle.behaviours.tick.TickMiddleBehaviourOpenAI;
import org.zeusagents.agents.middle.config.*;

public class MiddleOpenAIAgent extends Agent {

    private MiddleOpenAIConfig middleOpenAIConfig;

    protected void setup() {
        System.out.println("[Middle OpenAPI Agent] ReceiverAgent " + getAID().getName() + " is ready");
        this.setQueueSize(10);
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
            CyclicMiddleOpenAIConfig cyclicMiddleOpenAIConfig = (CyclicMiddleOpenAIConfig) middleOpenAIConfig;
            addBehaviour(CyclicMiddleBehaviourOpenAI.builder().agent(this).openAIClient(middleOpenAIConfig.getOpenAIClient()).build());
        }

        if(middleOpenAIConfig.getMiddleBehaviourType().equals(MiddleBehaviourType.SIMPLE_MIDDLE_BEHAVIOUR_OPENAI)){
            SimpleMiddleOpenAIConfig simpleMiddleOpenAIConfig = (SimpleMiddleOpenAIConfig) middleOpenAIConfig;
            addBehaviour(SimpleMiddleBehaviourOpenAI.builder().agent(this).openAIClient(middleOpenAIConfig.getOpenAIClient()).maxReceived(simpleMiddleOpenAIConfig.getMaxReceived()).build());
        }

        if(middleOpenAIConfig.getMiddleBehaviourType().equals(MiddleBehaviourType.TICK_MIDDLE_BEHAVIOUR_OPENAI)){
            TickMiddleOpenAIConfig tickMiddleOpenAIConfig = (TickMiddleOpenAIConfig) middleOpenAIConfig;
            addBehaviour(TickMiddleBehaviourOpenAI.builder().agent(this).openAIClient(middleOpenAIConfig.getOpenAIClient()).period(tickMiddleOpenAIConfig.getPeriod()).build());
        }
    }
}
