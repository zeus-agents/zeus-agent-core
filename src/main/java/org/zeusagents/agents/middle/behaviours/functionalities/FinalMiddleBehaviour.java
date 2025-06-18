package org.zeusagents.agents.middle.behaviours.functionalities;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import lombok.Builder;
import org.zeusagents.outputclient.OutputClient;
import org.zeusagents.agents.middle.config.DataStoreKeys;

public class FinalMiddleBehaviour extends OneShotBehaviour {
    private final OutputClient outputClient;

    @Builder
    public FinalMiddleBehaviour(Agent agent, DataStore ds, OutputClient outputClient) {
        super(agent);
        this.setDataStore(ds);
        this.outputClient = outputClient;
    }

    @Override
    public void action() {
        String msg = (String) getDataStore().get(DataStoreKeys.OUTPUT_MESSAGE.name());

        this.outputClient.execute(msg, myAgent);
        System.out.println("[FSM-FINAL " + myAgent.getName() + "] Transformed");
    }
}
