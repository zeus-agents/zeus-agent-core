package org.zeusagents.agents.middle.behaviours.functionalities;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import org.zeusagents.agents.middle.MiddleOpenAIAgent;
import org.zeusagents.agents.middle.config.MiddleFuncBehaviourtype;
import org.zeusagents.agents.middle.config.MiddleMainConfig;

public class MiddleBehaviourSelector {

    public static Behaviour selectMiddleBehaviour(MiddleOpenAIAgent midAgent,  DataStore ds, MiddleMainConfig middleMainConfig, MiddleFuncBehaviourtype middleFuncBehaviourtype){

        return switch (middleFuncBehaviourtype) {
            case RECEIVER_BEHAVIOUR -> ReceiverMiddleBehaviour.builder().agent(midAgent).ds(ds).build();
            case FINAL_BEHAVIOUR -> FinalMiddleBehaviour.builder().agent(midAgent).ds(ds).build();
            case GENERATOR_BEHAVIOUR ->
                    GeneratorMiddleBehaviour.builder().agent(midAgent).ds(ds).AIClient(middleMainConfig.getAIClient()).build();
            default -> null;
        };
    }
}
