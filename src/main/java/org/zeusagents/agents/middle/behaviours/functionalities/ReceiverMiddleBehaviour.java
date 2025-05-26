package org.zeusagents.agents.middle.behaviours.functionalities;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.middle.config.DataStoreKeys;
import org.zeusagents.inputClient.InputClient;

public class ReceiverMiddleBehaviour extends OneShotBehaviour {
    private final InputClient inputClient;

    @Builder
    public ReceiverMiddleBehaviour(Agent agent, DataStore ds, InputClient inputClient) {
        super(agent);
        this.setDataStore(ds);
        this.inputClient = inputClient;
    }

    @Override
    public void action() {
        BasicMessageInputAgent data = inputClient.execute(getDataStore());

        getDataStore().put(DataStoreKeys.PROMPT.name(), data);
        System.out.println("[FSM-STORE " + myAgent.getName() + "] Stored message");
    }
}
