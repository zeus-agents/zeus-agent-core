package org.zeusagents.agents.middle.behaviours.main;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.middle.MiddleOpenAIAgent;
import org.zeusagents.agents.middle.behaviours.functionalities.FinalMiddleBehaviour;
import org.zeusagents.agents.middle.behaviours.functionalities.GeneratorMiddleBehaviour;
import org.zeusagents.agents.middle.behaviours.functionalities.ReceiverMiddleBehaviour;

public class CyclicFSMiddleMainBehaviour extends TickerBehaviour {


    @Builder
    public CyclicFSMiddleMainBehaviour(Agent midAgent) {
        super(midAgent,200);
    }

    @Override
    public void onTick() {

        MiddleOpenAIAgent midAgent = (MiddleOpenAIAgent) myAgent;

        if (!midAgent.isFSMRunning() && !midAgent.getMessageCacheQueue().isEmpty()) {
            ACLMessage msg = midAgent.getMessageCacheQueue().poll();

            if (msg != null) {
                FSMBehaviour fsm = new FSMBehaviour(){
                    public int onEnd() {
                        midAgent.setFSMRunning(false);
                        System.out.println("[FSM] Finished handling: " + msg.getContent());
                        return super.onEnd();
                    }
                };

                DataStore ds = new DataStore();
                ds.put("incoming-msg", msg);
                fsm.setDataStore(ds);

                fsm.registerFirstState(new ReceiverMiddleBehaviour(this.myAgent,ds), "RECEIVER");
                fsm.registerState(new GeneratorMiddleBehaviour(this.myAgent,ds), "GENERATOR");
                fsm.registerLastState(new FinalMiddleBehaviour(this.myAgent,ds), "LAST");

                fsm.registerDefaultTransition("RECEIVER", "GENERATOR");
                fsm.registerDefaultTransition("GENERATOR", "LAST");

                this.myAgent.addBehaviour(fsm);
                midAgent.setFSMRunning(true);
            } else {
                System.out.println("[Middle OpenAPI Agent "+ myAgent.getName()+"] No message received, blocking");
            }
        }

        myAgent.removeBehaviour(this);
    }
}
