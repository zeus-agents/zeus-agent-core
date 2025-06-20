package org.zeusagents.agents.middle;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.MiddleAgentPool;
import org.zeusagents.agents.middle.behaviours.main.CyclicMiddleMainBehaviour;
import org.zeusagents.agents.middle.behaviours.main.SimpleMiddleMainBehaviour;
import org.zeusagents.agents.middle.behaviours.main.TickMiddleMainBehaviour;
import org.zeusagents.agents.middle.behaviours.schema.MiddleFSMBehaviour;
import org.zeusagents.agents.middle.config.*;

import java.util.LinkedList;
import java.util.Queue;

@Getter
public class MiddleAgent extends Agent {

    private final Queue<ACLMessage> messageCacheQueue = new LinkedList<>();
    private MiddleMainConfig middleMainConfig;
    @Setter
    private boolean isFSMRunning = false;

    protected void setup() {
        System.out.println("[Middle  Agent] ReceiverAgent " + getAID().getName() + " is ready");

            this.setQueueSize(10);
            final Object[] args = getArguments();

            if(null != args){
                middleMainConfig = (MiddleMainConfig) args[0];
                selectBehaviours();
                System.out.println("[Middle  Agent] SETUP COMPLETE");
            } else {
                System.out.println("[Middle  Agent] SETUP ERROR!");
            }

            if(middleMainConfig.isBalance()){
                System.out.println("[Middle  Agent] LocalName: " + this.getLocalName());
                MiddleAgentPool.addAgent(this.getLocalName());
                MiddleAgentPool.printAgentMap();
            }
        }

        private void selectBehaviours(){
            if(middleMainConfig.getMiddleMainBehaviourType().equals(MiddleMainBehaviourType.CYCLIC)){
                CyclicMiddleMainConfig cyclicMiddleMainConfig = (CyclicMiddleMainConfig) middleMainConfig;
                addBehaviour(CyclicMiddleMainBehaviour.builder().agent(this).build());
                addBehaviour(MiddleFSMBehaviour.builder().midAgent(this).period(cyclicMiddleMainConfig.getFsmPeriod()).build());
            }

            if(middleMainConfig.getMiddleMainBehaviourType().equals(MiddleMainBehaviourType.SIMPLE)){
                SimpleMiddleMainConfig simpleMiddleConfig = (SimpleMiddleMainConfig) middleMainConfig;
                addBehaviour(SimpleMiddleMainBehaviour.builder().agent(this).maxReceived(simpleMiddleConfig.getMaxReceived()).build());
                addBehaviour(MiddleFSMBehaviour.builder().midAgent(this).period(simpleMiddleConfig.getFsmPeriod()).build());
            }

            if(middleMainConfig.getMiddleMainBehaviourType().equals(MiddleMainBehaviourType.TICK)){
                TickMiddleMainConfig tickMiddleConfig = (TickMiddleMainConfig) middleMainConfig;
                addBehaviour(TickMiddleMainBehaviour.builder().agent(this).period(tickMiddleConfig.getPeriod()).build());
                addBehaviour(MiddleFSMBehaviour.builder().midAgent(this).period(tickMiddleConfig.getFsmPeriod()).build());
        }
    }
}
