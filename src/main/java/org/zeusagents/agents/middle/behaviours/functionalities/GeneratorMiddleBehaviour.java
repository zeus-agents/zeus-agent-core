package org.zeusagents.agents.middle.behaviours.functionalities;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.AIClient.AIClient;
import org.zeusagents.agents.middle.config.DataStoreKeys;

public class GeneratorMiddleBehaviour extends OneShotBehaviour {

    private final AIClient AIClient;

    @Builder
    public GeneratorMiddleBehaviour(Agent agent, DataStore ds, AIClient AIClient) {
        super(agent);
        this.setDataStore(ds);
        this.AIClient = AIClient;
    }

    @Override
    public void action() {
        BasicMessageInputAgent input = (BasicMessageInputAgent) getDataStore().get(DataStoreKeys.PROMPT.name());

        String execute = AIClient.execute(input.getContent());

        getDataStore().put(DataStoreKeys.OUTPUT_MESSAGE.name(), execute);
        System.out.println("[FSM-PROCESS " + myAgent.getName() + "] Transformed");

    }
}
