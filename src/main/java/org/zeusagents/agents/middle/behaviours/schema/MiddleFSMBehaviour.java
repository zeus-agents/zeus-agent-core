package org.zeusagents.agents.middle.behaviours.schema;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.middle.MiddleAgent;
import org.zeusagents.agents.middle.config.DataStoreKeys;
import org.zeusagents.agents.middle.config.MiddleFuncBehaviourtype;

import java.util.Map;

public class MiddleFSMBehaviour extends TickerBehaviour {


    @Builder
    public MiddleFSMBehaviour(Agent midAgent, long period) {
        super(midAgent, period);
    }

    @Override
    public void onTick() {

        MiddleAgent midAgent = (MiddleAgent) myAgent;

        if (!midAgent.isFSMRunning() && !midAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage msg = midAgent.getMessageCacheQueue().poll();

            if (msg != null) {
                FSMBehaviour fsm = new FSMBehaviour() {
                    public int onEnd() {
                        midAgent.setFSMRunning(false);
                        System.out.println("[FSM "+ midAgent.getName() +"] Finished handling: " + msg.getContent());
                        return super.onEnd();
                    }
                };

                DataStore ds = new DataStore();
                ds.put(DataStoreKeys.INPUT_MESSAGE.name(), msg);
                fsm.setDataStore(ds);

                this.myAgent.addBehaviour(createFSMSequence(midAgent, fsm, ds));
                midAgent.setFSMRunning(true);
            } else {
                System.out.println("[Middle  Agent " + myAgent.getName() + "] No message received, blocking");
            }
        } else {
            block();
        }

    }

    private FSMBehaviour createFSMSequence(MiddleAgent midAgent, FSMBehaviour fsm, DataStore ds) {
        int i = 0;
        String formerBehaviour = null;

        for(Map.Entry<MiddleFuncBehaviourtype, Object> entry: midAgent.getMiddleMainConfig().getOrderBehaviourWithClient().entrySet()){

            if(i > 0){

                if(i >= midAgent.getMiddleMainConfig().getOrderBehaviourWithClient().size()-1){
                    fsm.registerLastState(MiddleBehaviourSelector.selectMiddleBehaviour(midAgent, ds, entry.getKey(), entry.getValue()), entry.getKey().name()+i);
                } else {
                    fsm.registerState(MiddleBehaviourSelector.selectMiddleBehaviour(midAgent, ds, entry.getKey(), entry.getValue()), entry.getKey().name()+i);
                }

                fsm.registerDefaultTransition(formerBehaviour+(i-1), entry.getKey().name()+ i);

            } else {
                fsm.registerFirstState(MiddleBehaviourSelector.selectMiddleBehaviour(midAgent, ds, entry.getKey(), entry.getValue()), entry.getKey().name()+i);
            }

            formerBehaviour = entry.getKey().name();
            i++;
        }

        return fsm;
    }
}
