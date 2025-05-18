package org.zeusagents.agents.input;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import org.zeusagents.agents.input.behaviours.cyclic.CyclicReceiverInputBehaviourOpenAI;
import org.zeusagents.agents.input.behaviours.cyclic.CyclicSenderInputBehaviourOpenAI;
import org.zeusagents.agents.input.behaviours.simple.SimpleReceiverInputBehaviourOpenAI;
import org.zeusagents.agents.input.behaviours.simple.SimpleSenderInputBehaviourOpenAI;
import org.zeusagents.agents.input.behaviours.tick.TickReceiverInputBehaviourOpenAI;
import org.zeusagents.agents.input.behaviours.tick.TickSenderInputBehaviourOpenAI;
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.input.config.InputOpenAIConfig;

import java.util.*;

@Getter
public class InputOpenAIAgent extends Agent {
    Queue<ACLMessage> messageCacheQueue = new LinkedList<>();
    private InputOpenAIConfig inputOpenAIConfig;

    protected void setup() {
        System.out.println("[Input OpenAPI Agent] ReceiverAgent " + getAID().getName() + " is ready");
        final Object[] args = getArguments();

        if(null != args){
            inputOpenAIConfig = (InputOpenAIConfig) args[0];
            //This accept data external from the JDE system
            setEnabledO2ACommunication(true, 10);

            selectBehaviour();
            System.out.println("[Input OpenAPI Agent] SETUP COMPLETE");
        } else{
            System.out.println("[Input OpenAPI Agent] SETUP ERROR!");
        }


    }

    private void selectBehaviour(){
        if(inputOpenAIConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.CYCLIC_INPUT_BEHAVIOUR_OPENAI)){
            //In this case the Cyclic Sender is created by the Receiver to upgrade the CPU performance.
            addBehaviour(CyclicReceiverInputBehaviourOpenAI.builder().inputAgent(this).build());
        }

        if(inputOpenAIConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.SIMPLE_INPUT_BEHAVIOUR_OPENAI)){
            addBehaviour(SimpleReceiverInputBehaviourOpenAI.builder().inputAgent(this).build());
            addBehaviour(SimpleSenderInputBehaviourOpenAI.builder().inputAgent(this).build());
        }

        if(inputOpenAIConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.TICK_INPUT_BEHAVIOUR_OPENAI)){
            addBehaviour(TickReceiverInputBehaviourOpenAI.builder().inputAgent(this).build());
            addBehaviour(TickSenderInputBehaviourOpenAI.builder().inputAgent(this).build());
        }
    }
}
