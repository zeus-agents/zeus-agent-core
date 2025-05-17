package org.zeusagents.agents.input;

import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;
import org.zeusagents.agents.input.behaviours.cyclic.CyclicInputBehaviourOpenAI;
import org.zeusagents.agents.input.config.InputOpenAIConfig;
import org.zeusagents.openai.OpenAITextGeneratorClient;

@Slf4j
public class InputOpenAIAgent extends Agent {
    InputOpenAIConfig inputOpenAIConfig;

    protected void setup() {
        System.out.println("ReceiverAgent " + getAID().getName() + " is ready");
        final Object[] args = getArguments();

        if(null != args){
            inputOpenAIConfig = (InputOpenAIConfig) args[0];
            inputOpenAIConfig.getMiddleAgents().forEach(System.out::println);
        }

        //This accept data external from the JDE system
        setEnabledO2ACommunication(true, 0);

        addBehaviour(CyclicInputBehaviourOpenAI.builder().inputAgent(this).middelAgentName(inputOpenAIConfig.getMiddleAgents().get(0)).build());
        System.out.println("[Input OpenAPI Agent] SETUP COMPLETE");
    }
}
