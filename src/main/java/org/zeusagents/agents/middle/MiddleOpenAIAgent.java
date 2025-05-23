package org.zeusagents.agents.middle;

import jade.core.Agent;
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
            if(middleMainConfig.getMiddleMainBehaviourType().equals(MiddleMainBehaviourType.CYCLIC)){
                CyclicMiddleMainConfig cyclicMiddleMainConfig = (CyclicMiddleMainConfig) middleMainConfig;
                addBehaviour(CyclicMiddleMainBehaviour.builder().agent(this).build());
            }

            if(middleMainConfig.getMiddleMainBehaviourType().equals(MiddleMainBehaviourType.SIMPLE)){
                SimpleMiddleMainConfig simpleMiddleOpenAIConfig = (SimpleMiddleMainConfig) middleMainConfig;
                addBehaviour(SimpleMiddleMainBehaviour.builder().agent(this).maxReceived(simpleMiddleOpenAIConfig.getMaxReceived()).build());
            }

            if(middleMainConfig.getMiddleMainBehaviourType().equals(MiddleMainBehaviourType.TICK)){
                TickMiddleMainConfig tickMiddleOpenAIConfig = (TickMiddleMainConfig) middleMainConfig;
                addBehaviour(TickMiddleMainBehaviour.builder().agent(this).period(tickMiddleOpenAIConfig.getPeriod()).build());
        }
    }
}
