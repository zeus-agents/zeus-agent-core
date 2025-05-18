package org.zeusagents.agents.input;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.zeusagents.agents.input.behaviours.cyclic.CyclicInputBehaviourOpenAI;
import org.zeusagents.agents.input.behaviours.simple.SimpleInputBehaviourOpenAI;
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

            activateBehaviours();
            System.out.println("[Input OpenAPI Agent] SETUP COMPLETE");
        } else{
            System.out.println("[Input OpenAPI Agent] SETUP ERROR!");
        }


    }

    private void activateBehaviours(){
        if(inputOpenAIConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.CYCLIC_INPUT_BEHAVIOUR_OPENAI)){
            addBehaviour(CyclicInputBehaviourOpenAI.builder().inputAgent(this).build());
        }

        if(inputOpenAIConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.SIMPLE_INPUT_BEHAVIOUR_OPENAI)){
            addBehaviour(SimpleInputBehaviourOpenAI.builder().inputAgent(this).build());
        }

        if(inputOpenAIConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.TICK_INPUT_BEHAVIOUR_OPENAI)){
            addBehaviour(TickReceiverInputBehaviourOpenAI.builder().inputAgent(this).build());
            addBehaviour(TickSenderInputBehaviourOpenAI.builder().inputAgent(this).build());
        }
    }
}
