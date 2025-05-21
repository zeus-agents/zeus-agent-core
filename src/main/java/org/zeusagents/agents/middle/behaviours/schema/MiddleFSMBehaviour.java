package org.zeusagents.agents.middle.behaviours.schema;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.middle.MiddleOpenAIAgent;
import org.zeusagents.agents.middle.behaviours.functionalities.MiddleBehaviourSelector;
import org.zeusagents.agents.middle.config.MiddleMainConfig;

public class MiddleFSMBehaviour extends TickerBehaviour {


    @Builder
    public MiddleFSMBehaviour(Agent midAgent) {
        super(midAgent, 200);
    }

    @Override
    public void onTick() {

        MiddleOpenAIAgent midAgent = (MiddleOpenAIAgent) myAgent;

        if (!midAgent.isFSMRunning() && !midAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage msg = midAgent.getMessageCacheQueue().poll();

            if (msg != null) {
                FSMBehaviour fsm = new FSMBehaviour() {
                    public int onEnd() {
                        midAgent.setFSMRunning(false);
                        System.out.println("[FSM] Finished handling: " + msg.getContent());
                        return super.onEnd();
                    }
                };

                DataStore ds = new DataStore();
                ds.put("first-msg", msg);
                fsm.setDataStore(ds);

                this.myAgent.addBehaviour(createFSMSequence(midAgent, fsm, ds, midAgent.getMiddleMainConfig()));
                midAgent.setFSMRunning(true);
            } else {
                System.out.println("[Middle OpenAPI Agent " + myAgent.getName() + "] No message received, blocking");
            }

            myAgent.removeBehaviour(this);
        }

    }

    private FSMBehaviour createFSMSequence(MiddleOpenAIAgent midAgent, FSMBehaviour fsm, DataStore ds, MiddleMainConfig middleMainConfig) {
        int size = middleMainConfig.getOrderList().size();
        int lastObj = size - 1;

        fsm.registerFirstState(MiddleBehaviourSelector.selectMiddleBehaviour(midAgent, ds, middleMainConfig, middleMainConfig.getOrderList().get(0)), middleMainConfig.getOrderList().get(0).name());

        for (int i = 1; size - 1 > i; i++) {
            fsm.registerState(MiddleBehaviourSelector.selectMiddleBehaviour(midAgent, ds, middleMainConfig, middleMainConfig.getOrderList().get(i)), middleMainConfig.getOrderList().get(i).name() + i);

            fsm.registerDefaultTransition(middleMainConfig.getOrderList().get(i - 1).name(), middleMainConfig.getOrderList().get(i).name() + i);
        }

        fsm.registerLastState(MiddleBehaviourSelector.selectMiddleBehaviour(midAgent, ds, middleMainConfig, middleMainConfig.getOrderList().get(lastObj)), middleMainConfig.getOrderList().get(lastObj).name() + lastObj);
        fsm.registerDefaultTransition(middleMainConfig.getOrderList().get(lastObj - 1).name() + (lastObj - 1), middleMainConfig.getOrderList().get(lastObj).name() + lastObj);

        return fsm;
    }
}
