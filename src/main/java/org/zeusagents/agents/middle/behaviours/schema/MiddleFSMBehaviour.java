package org.zeusagents.agents.middle.behaviours.schema;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.middle.MiddleOpenAIAgent;
import org.zeusagents.agents.middle.behaviours.functionalities.MiddleBehaviourSelector;
import org.zeusagents.agents.middle.config.MiddleFuncBehaviourtype;
import org.zeusagents.agents.middle.config.MiddleMainConfig;

import java.util.Map;

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
        int i = 0;
        String formerBehaviour = null;

        for(Map.Entry<MiddleFuncBehaviourtype, Object> entry: middleMainConfig.getOrderBehaviourWithClient().entrySet()){

            if(i > 0){

                if(i >= middleMainConfig.getOrderBehaviourWithClient().size()-1){
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
