package org.zeusagents.agents.middle.behaviours.functionalities;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import lombok.Builder;

public class FinalMiddleBehaviour extends OneShotBehaviour {

    @Builder
    public FinalMiddleBehaviour(Agent agent, DataStore ds) {
        super(agent);
        this.setDataStore(ds);
    }

    @Override
    public void action() {
        String msg = (String) getDataStore().get("result");
        AID sender = (AID) getDataStore().get("sender");

        System.out.println("=== Received Message ===");
        System.out.println("Sender: " + sender);
        System.out.println("Content: " + msg);
        System.out.println("Agent: " + this.myAgent.getAID());
        System.out.println("=======================");
    }
}
