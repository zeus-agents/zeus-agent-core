package org.zeusagents.agents.middle.behaviours.functionalities;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;

public class GeneratorMiddleBehaviour extends OneShotBehaviour {
    @Builder
    public GeneratorMiddleBehaviour(Agent agent, DataStore ds) {
        super(agent);
        this.setDataStore(ds);
    }

    @Override
    public void action() {
        BasicMessageInputAgent input = (BasicMessageInputAgent) getDataStore().get("content");

        getDataStore().put("result", input.getContent());
        System.out.println("[FSM-PROCESS] " + myAgent.getName()+ "] Transformed");

    }
}
