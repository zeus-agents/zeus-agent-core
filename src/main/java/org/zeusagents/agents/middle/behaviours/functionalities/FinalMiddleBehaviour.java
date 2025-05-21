package org.zeusagents.agents.middle.behaviours.functionalities;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import lombok.Builder;
import org.zeusagents.OutputClient.OutputClient;

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
        String msg = (String) getDataStore().get("result");
        //AID sender = (AID) getDataStore().get("sender");

        this.outputClient.execute(msg, myAgent);
    }
}
