package org.zeusagents.agents.middle.behaviours.schema;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import org.zeusagents.AIClient.AIClient;
import org.zeusagents.OutputClient.OutputClient;
import org.zeusagents.agents.middle.MiddleAgent;
import org.zeusagents.agents.middle.behaviours.functionalities.FinalMiddleBehaviour;
import org.zeusagents.agents.middle.behaviours.functionalities.GeneratorMiddleBehaviour;
import org.zeusagents.agents.middle.behaviours.functionalities.ReceiverMiddleBehaviour;
import org.zeusagents.agents.middle.config.MiddleFuncBehaviourtype;
import org.zeusagents.inputClient.InputClient;

public class MiddleBehaviourSelector {

    public static Behaviour selectMiddleBehaviour(MiddleAgent midAgent, DataStore ds, MiddleFuncBehaviourtype middleFuncBehaviourtype, Object client) {

        return switch (middleFuncBehaviourtype) {
            case RECEIVER_BEHAVIOUR -> ReceiverMiddleBehaviour.builder().agent(midAgent).ds(ds).inputClient((InputClient) client).build();
            case FINAL_BEHAVIOUR -> FinalMiddleBehaviour.builder().agent(midAgent).ds(ds).outputClient((OutputClient) client).build();
            case GENERATOR_BEHAVIOUR ->
                    GeneratorMiddleBehaviour.builder().agent(midAgent).ds(ds).AIClient((AIClient) client).build();
        };
    }
}
