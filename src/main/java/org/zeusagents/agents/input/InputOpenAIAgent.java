package org.zeusagents.agents.input;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.zeusagents.agents.input.behaviours.cyclic.CyclicInputBehaviourOpenAI;
import org.zeusagents.agents.input.behaviours.simple.SimpleInputBehaviourOpenAI;
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.input.config.InputOpenAIConfig;

import java.util.Map;

@Slf4j
public class InputOpenAIAgent extends Agent {
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
        for(Map.Entry<String, InputBehaviourTypes> entry : inputOpenAIConfig.getBehaviourForMiddleAgent().entrySet()){
            if(entry.getValue().equals(InputBehaviourTypes.CYCLIC_INPUT_BEHAVIOUR_OPENAI)){
                addBehaviour(CyclicInputBehaviourOpenAI.builder().inputAgent(this).middelAgentName(entry.getKey()).build());
            }

            if(entry.getValue().equals(InputBehaviourTypes.SIMPLE_INPUT_BEHAVIOUR_OPENAI)){
                addBehaviour(SimpleInputBehaviourOpenAI.builder().inputAgent(this).middelAgentName(entry.getKey()).build());
            }
        }

    }
}
