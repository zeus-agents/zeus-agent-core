package org.zeusagents.agents.middle;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.middle.behaviours.main.CyclicMiddleMainBehaviour;
import org.zeusagents.agents.middle.behaviours.main.SimpleMiddleMainBehaviour;
import org.zeusagents.agents.middle.behaviours.main.TickMiddleMainBehaviour;
import org.zeusagents.agents.middle.config.*;

import java.util.LinkedList;
import java.util.Queue;

@Getter
public class MiddleOpenAIAgent extends Agent {

    private final Queue<ACLMessage> messageCacheQueue = new LinkedList<>();
    private MiddleMainConfig middleMainConfig;
    @Setter
    private boolean isFSMRunning = false;

    protected void setup() {
        System.out.println("[Middle OpenAPI Agent] ReceiverAgent " + getAID().getName() + " is ready");


        this.setQueueSize(10);
        final Object[] args = getArguments();

        if(null != args){
            middleMainConfig = (MiddleMainConfig) args[0];
            activateBehaviours();
            System.out.println("[Middle OpenAPI Agent] SETUP COMPLETE");
        } else {
            System.out.println("[Middle OpenAPI Agent] SETUP ERROR!");
        }
    }

    private void activateBehaviours(){
        if(middleMainConfig.getMiddleBehaviourType().equals(MiddleBehaviourType.CYCLIC_MIDDLE_BEHAVIOUR_OPENAI)){
            CyclicMiddleMainConfig cyclicMiddleMainConfig = (CyclicMiddleMainConfig) middleMainConfig;
            addBehaviour(CyclicMiddleMainBehaviour.builder().agent(this).AIClient(middleMainConfig.getAIClient()).build());
        }

        if(middleMainConfig.getMiddleBehaviourType().equals(MiddleBehaviourType.SIMPLE_MIDDLE_BEHAVIOUR_OPENAI)){
            SimpleMiddleMainConfig simpleMiddleOpenAIConfig = (SimpleMiddleMainConfig) middleMainConfig;
            addBehaviour(SimpleMiddleMainBehaviour.builder().agent(this).AIClient(middleMainConfig.getAIClient()).maxReceived(simpleMiddleOpenAIConfig.getMaxReceived()).build());
        }

        if(middleMainConfig.getMiddleBehaviourType().equals(MiddleBehaviourType.TICK_MIDDLE_BEHAVIOUR_OPENAI)){
            TickMiddleMainConfig tickMiddleOpenAIConfig = (TickMiddleMainConfig) middleMainConfig;
            addBehaviour(TickMiddleMainBehaviour.builder().agent(this).AIClient(middleMainConfig.getAIClient()).period(tickMiddleOpenAIConfig.getPeriod()).build());
        }
    }
}
